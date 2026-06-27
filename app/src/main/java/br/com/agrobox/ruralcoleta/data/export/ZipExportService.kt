package br.com.agrobox.ruralcoleta.data.export

import android.content.Context
import android.net.Uri
import br.com.agrobox.ruralcoleta.data.local.entity.BenfeitoriaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.FotoBenfeitoriaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.FotoColetaEntity
import br.com.agrobox.ruralcoleta.domain.exportacao.DadosExportacao
import java.io.File
import java.io.FileOutputStream
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ZipExportService {

    fun exportarZipCompleto(
        context: Context,
        dados: DadosExportacao,
        excelExportService: ExcelExportService
    ): File {
        val dataHora = SimpleDateFormat(
            "yyyy-MM-dd_HHmmss",
            Locale("pt", "BR")
        ).format(Date())

        val nomeBase = "RuralColeta_$dataHora"

        val pastaExportacoes = File(
            context.filesDir,
            "exportacoes"
        )

        if (!pastaExportacoes.exists()) {
            pastaExportacoes.mkdirs()
        }

        val pastaTemporaria = File(
            pastaExportacoes,
            nomeBase
        )

        if (pastaTemporaria.exists()) {
            pastaTemporaria.deleteRecursively()
        }

        pastaTemporaria.mkdirs()

        val arquivoExcel = excelExportService.exportarColetas(
            context = context,
            nomeModelo = dados.nomeModelo,
            coletas = dados.coletas,
            variaveis = dados.variaveis,
            respostasPorColeta = dados.respostasPorColeta,
            fotoPrincipalPorColeta = dados.fotoPrincipalPorColeta,
            fotosBenfeitoriasPorColeta = dados.fotosBenfeitoriasPorColeta
        )

        arquivoExcel.copyTo(
            target = File(
                pastaTemporaria,
                "Dados_Coleta.xlsx"
            ),
            overwrite = true
        )

        criarEstruturaDeFotos(
            context = context,
            pastaTemporaria = pastaTemporaria,
            dados = dados
        )

        val arquivoZip = File(
            pastaExportacoes,
            "$nomeBase.zip"
        )

        if (arquivoZip.exists()) {
            arquivoZip.delete()
        }

        compactarPasta(
            pastaOrigem = pastaTemporaria,
            arquivoZip = arquivoZip
        )

        pastaTemporaria.deleteRecursively()

        return arquivoZip
    }

    private fun criarEstruturaDeFotos(
        context: Context,
        pastaTemporaria: File,
        dados: DadosExportacao
    ) {
        val exportacaoUnica = dados.coletas.size == 1

        dados.coletas.forEachIndexed { index, coleta ->
            val pastaRaizColeta = if (exportacaoUnica) {
                pastaTemporaria
            } else {
                File(
                    pastaTemporaria,
                    "${(index + 1).toString().padStart(2, '0')}_${nomeSeguro(coleta.nomeReferencia)}"
                ).apply {
                    mkdirs()
                }
            }

            copiarFotosGerais(
                context = context,
                pastaRaizColeta = pastaRaizColeta,
                fotosGerais = dados.fotosGeraisPorColeta[coleta.id].orEmpty()
            )

            copiarFotosBenfeitorias(
                context = context,
                pastaRaizColeta = pastaRaizColeta,
                benfeitorias = dados.benfeitoriasPorColeta[coleta.id].orEmpty(),
                fotosPorBenfeitoria = dados.fotosPorBenfeitoria
            )
        }
    }

    private fun copiarFotosGerais(
        context: Context,
        pastaRaizColeta: File,
        fotosGerais: List<FotoColetaEntity>
    ) {
        val pastaFotosGerais = File(
            pastaRaizColeta,
            "Fotos_Gerais"
        )

        pastaFotosGerais.mkdirs()

        fotosGerais.forEachIndexed { index, foto ->
            val extensao = extensaoFoto(foto.caminhoArquivo)

            copiarFoto(
                context = context,
                caminhoOrigem = foto.caminhoArquivo,
                destino = File(
                    pastaFotosGerais,
                    "Foto_Geral_${(index + 1).toString().padStart(3, '0')}.$extensao"
                )
            )
        }
    }

    private fun copiarFotosBenfeitorias(
        context: Context,
        pastaRaizColeta: File,
        benfeitorias: List<BenfeitoriaEntity>,
        fotosPorBenfeitoria: Map<Long, List<FotoBenfeitoriaEntity>>
    ) {
        val pastaBenfeitorias = File(
            pastaRaizColeta,
            "Benfeitorias"
        )

        pastaBenfeitorias.mkdirs()

        benfeitorias.forEach { benfeitoria ->
            val nomePasta = nomeSeguro(
                benfeitoria.nome.ifBlank {
                    benfeitoria.categoria.ifBlank {
                        "Benfeitoria_${benfeitoria.id}"
                    }
                }
            )

            val pastaBenfeitoria = File(
                pastaBenfeitorias,
                nomePasta
            )

            pastaBenfeitoria.mkdirs()

            val fotos = fotosPorBenfeitoria[benfeitoria.id].orEmpty()

            fotos.forEachIndexed { index, foto ->
                val extensao = extensaoFoto(foto.caminhoArquivo)

                copiarFoto(
                    context = context,
                    caminhoOrigem = foto.caminhoArquivo,
                    destino = File(
                        pastaBenfeitoria,
                        "${nomePasta}_${(index + 1).toString().padStart(3, '0')}.$extensao"
                    )
                )
            }
        }
    }

    private fun copiarFoto(
        context: Context,
        caminhoOrigem: String,
        destino: File
    ) {
        try {
            destino.parentFile?.mkdirs()

            if (caminhoOrigem.startsWith("content://")) {
                context.contentResolver
                    .openInputStream(Uri.parse(caminhoOrigem))
                    ?.use { input ->
                        FileOutputStream(destino).use { output ->
                            input.copyTo(output)
                        }
                    }

                return
            }

            val origem = File(caminhoOrigem)

            if (!origem.exists()) {
                return
            }

            origem.copyTo(
                target = destino,
                overwrite = true
            )
        } catch (_: Exception) {
            // Se uma foto não for encontrada, a exportação continua com as demais.
        }
    }

    private fun compactarPasta(
        pastaOrigem: File,
        arquivoZip: File
    ) {
        ZipOutputStream(
            FileOutputStream(arquivoZip)
        ).use { zipOut ->

            pastaOrigem.walkTopDown().forEach { arquivo ->
                if (arquivo == pastaOrigem) {
                    return@forEach
                }

                val caminhoRelativo = arquivo
                    .relativeTo(pastaOrigem)
                    .path
                    .replace("\\", "/")

                if (arquivo.isDirectory) {
                    zipOut.putNextEntry(
                        ZipEntry("$caminhoRelativo/")
                    )
                    zipOut.closeEntry()
                } else {
                    zipOut.putNextEntry(
                        ZipEntry(caminhoRelativo)
                    )

                    arquivo.inputStream().use { input ->
                        input.copyTo(zipOut)
                    }

                    zipOut.closeEntry()
                }
            }
        }
    }

    private fun nomeSeguro(
        texto: String
    ): String {
        val semAcentos = Normalizer
            .normalize(texto, Normalizer.Form.NFD)
            .replace(Regex("\\p{Mn}+"), "")

        return semAcentos
            .trim()
            .ifBlank { "Sem_nome" }
            .replace(Regex("\\s+"), "_")
            .replace(Regex("[^A-Za-z0-9_-]"), "_")
            .replace(Regex("_+"), "_")
            .take(80)
    }

    private fun extensaoFoto(
        caminho: String
    ): String {
        val extensao = if (caminho.startsWith("content://")) {
            "jpg"
        } else {
            File(caminho).extension.lowercase()
        }

        return when (extensao) {
            "png" -> "png"
            "webp" -> "webp"
            "jpeg" -> "jpg"
            "jpg" -> "jpg"
            else -> "jpg"
        }
    }
}
