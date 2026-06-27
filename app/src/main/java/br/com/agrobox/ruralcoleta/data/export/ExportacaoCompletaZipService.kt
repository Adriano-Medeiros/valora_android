package br.com.agrobox.ruralcoleta.data.export

import android.content.Context
import br.com.agrobox.ruralcoleta.data.local.entity.BenfeitoriaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.ColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.FotoBenfeitoriaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.FotoColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.RespostaColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.VariavelEntity
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ExportacaoCompletaZipService {

    fun exportarZipCompleto(
        context: Context,
        nomeModelo: String,
        coletas: List<ColetaEntity>,
        variaveis: List<VariavelEntity>,
        respostasPorColeta: Map<Long, List<RespostaColetaEntity>>,
        fotoPrincipalPorColeta: Map<Long, String?>,
        fotosBenfeitoriasPorColeta: Map<Long, List<String>>,
        fotosGeraisPorColeta: Map<Long, List<FotoColetaEntity>>,
        benfeitoriasPorColeta: Map<Long, List<BenfeitoriaEntity>>,
        fotosPorBenfeitoria: Map<Long, List<FotoBenfeitoriaEntity>>
    ): File {
        val dataHora = SimpleDateFormat(
            "yyyy-MM-dd_HHmmss",
            Locale("pt", "BR")
        ).format(Date())

        val nomeBase = "RuralColeta_$dataHora"

        val pastaExportacoes = File(context.filesDir, "exportacoes")

        if (!pastaExportacoes.exists()) {
            pastaExportacoes.mkdirs()
        }

        val pastaTemporaria = File(pastaExportacoes, nomeBase)

        if (pastaTemporaria.exists()) {
            pastaTemporaria.deleteRecursively()
        }

        pastaTemporaria.mkdirs()

        val arquivoExcelOriginal = ExcelExportService().exportarColetas(
            context = context,
            nomeModelo = nomeModelo,
            coletas = coletas,
            variaveis = variaveis,
            respostasPorColeta = respostasPorColeta,
            fotoPrincipalPorColeta = fotoPrincipalPorColeta,
            fotosBenfeitoriasPorColeta = fotosBenfeitoriasPorColeta
        )

        arquivoExcelOriginal.copyTo(
            target = File(pastaTemporaria, "Dados_Coleta.xlsx"),
            overwrite = true
        )

        criarPastasEFotos(
            pastaTemporaria = pastaTemporaria,
            coletas = coletas,
            fotosGeraisPorColeta = fotosGeraisPorColeta,
            benfeitoriasPorColeta = benfeitoriasPorColeta,
            fotosPorBenfeitoria = fotosPorBenfeitoria
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

    private fun criarPastasEFotos(
        pastaTemporaria: File,
        coletas: List<ColetaEntity>,
        fotosGeraisPorColeta: Map<Long, List<FotoColetaEntity>>,
        benfeitoriasPorColeta: Map<Long, List<BenfeitoriaEntity>>,
        fotosPorBenfeitoria: Map<Long, List<FotoBenfeitoriaEntity>>
    ) {
        val exportacaoUnica = coletas.size == 1

        coletas.forEachIndexed { coletaIndex, coleta ->
            val pastaRaizColeta = if (exportacaoUnica) {
                pastaTemporaria
            } else {
                File(
                    pastaTemporaria,
                    "${(coletaIndex + 1).toString().padStart(2, '0')}_${nomeSeguro(coleta.nomeReferencia)}"
                ).apply {
                    mkdirs()
                }
            }

            copiarFotosGerais(
                pastaRaizColeta = pastaRaizColeta,
                coletaId = coleta.id,
                fotosGerais = fotosGeraisPorColeta[coleta.id].orEmpty()
            )

            copiarFotosBenfeitorias(
                pastaRaizColeta = pastaRaizColeta,
                benfeitorias = benfeitoriasPorColeta[coleta.id].orEmpty(),
                fotosPorBenfeitoria = fotosPorBenfeitoria
            )
        }
    }

    private fun copiarFotosGerais(
        pastaRaizColeta: File,
        coletaId: Long,
        fotosGerais: List<FotoColetaEntity>
    ) {
        val pastaFotosGerais = File(
            pastaRaizColeta,
            "Fotos_Gerais"
        )

        pastaFotosGerais.mkdirs()

        fotosGerais.forEachIndexed { index, foto ->
            copiarFoto(
                caminhoOrigem = foto.caminhoArquivo,
                destino = File(
                    pastaFotosGerais,
                    "Foto_Geral_${(index + 1).toString().padStart(3, '0')}_${coletaId}.${extensaoFoto(foto.caminhoArquivo)}"
                )
            )
        }
    }

    private fun copiarFotosBenfeitorias(
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
                copiarFoto(
                    caminhoOrigem = foto.caminhoArquivo,
                    destino = File(
                        pastaBenfeitoria,
                        "${nomePasta}_${(index + 1).toString().padStart(3, '0')}.${extensaoFoto(foto.caminhoArquivo)}"
                    )
                )
            }
        }
    }

    private fun copiarFoto(
        caminhoOrigem: String,
        destino: File
    ) {
        val origem = File(caminhoOrigem)

        if (!origem.exists()) {
            return
        }

        destino.parentFile?.mkdirs()

        origem.copyTo(
            target = destino,
            overwrite = true
        )
    }

    private fun compactarPasta(
        pastaOrigem: File,
        arquivoZip: File
    ) {
        ZipOutputStream(
            FileOutputStream(arquivoZip)
        ).use { zipOut ->

            pastaOrigem.walkTopDown()
                .filter { arquivo ->
                    arquivo.isFile
                }
                .forEach { arquivo ->
                    val caminhoRelativo = arquivo
                        .relativeTo(pastaOrigem)
                        .path
                        .replace("\\", "/")

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

    private fun nomeSeguro(
        texto: String
    ): String {
        return texto
            .trim()
            .ifBlank { "Sem_nome" }
            .replace(" ", "_")
            .replace("/", "_")
            .replace("\\", "_")
            .replace(":", "_")
            .replace("*", "_")
            .replace("?", "_")
            .replace("\"", "_")
            .replace("<", "_")
            .replace(">", "_")
            .replace("|", "_")
    }

    private fun extensaoFoto(
        caminho: String
    ): String {
        val extensao = File(caminho).extension.lowercase()

        return when (extensao) {
            "png" -> "png"
            "webp" -> "webp"
            else -> "jpg"
        }
    }
}