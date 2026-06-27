package br.com.agrobox.ruralcoleta.data.export

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.ContextCompat
import br.com.agrobox.ruralcoleta.R
import br.com.agrobox.ruralcoleta.data.local.entity.BenfeitoriaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.ColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.FotoBenfeitoriaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.FotoColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.RespostaColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.VariavelEntity
import br.com.agrobox.ruralcoleta.domain.exportacao.DadosExportacao
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

class PdfExportService {

    private val larguraPagina = 595
    private val alturaPagina = 842
    private val margem = 42f
    private val rodapeAltura = 42f
    private val larguraConteudo = larguraPagina - (margem * 2)

    fun exportarRelatorioPdf(
        context: Context,
        dados: DadosExportacao
    ): File {
        val pasta = File(
            context.filesDir,
            "exportacoes"
        )

        if (!pasta.exists()) {
            pasta.mkdirs()
        }

        val dataHoraArquivo = SimpleDateFormat(
            "yyyy-MM-dd_HHmmss",
            Locale("pt", "BR")
        ).format(Date())

        val nomeArquivo = if (dados.coletas.size == 1) {
            "relatorio_${nomeSeguro(dados.coletas.first().nomeReferencia)}_$dataHoraArquivo.pdf"
        } else {
            "relatorio_${nomeSeguro(dados.nomeModelo)}_$dataHoraArquivo.pdf"
        }

        val arquivo = File(
            pasta,
            nomeArquivo
        )

        val documento = PdfDocument()
        val escritor = EscritorPdf(documento)

        dados.coletas.forEachIndexed { index, coleta ->
            escritor.novaPagina()

            desenharRelatorioDaColeta(
                context = context,
                escritor = escritor,
                dados = dados,
                coleta = coleta,
                numeroColeta = index + 1,
                totalColetas = dados.coletas.size
            )
        }

        escritor.finalizar()

        FileOutputStream(arquivo).use { output ->
            documento.writeTo(output)
        }

        documento.close()

        return arquivo
    }

    private fun desenharRelatorioDaColeta(
        context: Context,
        escritor: EscritorPdf,
        dados: DadosExportacao,
        coleta: ColetaEntity,
        numeroColeta: Int,
        totalColetas: Int
    ) {
        escritor.cabecalhoRelatorio(
            context = context,
            titulo = "Relatório de Coleta Rural",
            subtitulo = if (totalColetas > 1) {
                "Coleta $numeroColeta de $totalColetas"
            } else {
                "Relatório individual da coleta"
            },
            nomeReferencia = coleta.nomeReferencia,
            tipo = formatarTipoColeta(coleta.tipoColeta),
            status = formatarStatus(coleta.status)
        )

        desenharResumoInicial(
            escritor = escritor,
            coleta = coleta,
            nomeFormulario = dados.modelo?.nome.orEmpty()
        )

        desenharDadosGerais(
            escritor = escritor,
            coleta = coleta
        )

        desenharRespostas(
            escritor = escritor,
            variaveis = dados.variaveis,
            respostas = dados.respostasPorColeta[coleta.id].orEmpty()
        )

        desenharBenfeitorias(
            escritor = escritor,
            benfeitorias = dados.benfeitoriasPorColeta[coleta.id].orEmpty()
        )

        desenharFotosGerais(
            context = context,
            escritor = escritor,
            fotos = dados.fotosGeraisPorColeta[coleta.id].orEmpty()
        )

        desenharFotosDasBenfeitorias(
            context = context,
            escritor = escritor,
            benfeitorias = dados.benfeitoriasPorColeta[coleta.id].orEmpty(),
            fotosPorBenfeitoria = dados.fotosPorBenfeitoria
        )
    }

    private fun desenharResumoInicial(
        escritor: EscritorPdf,
        coleta: ColetaEntity,
        nomeFormulario: String
    ) {
        escritor.cardResumo(
            titulo = "Resumo da coleta"
        ) {
            campoDuasColunas("Imóvel/referência", coleta.nomeReferencia)
            campoDuasColunas("Formulário", nomeFormulario.ifBlank { "Não informado" })
            campoDuasColunas("Tipo", formatarTipoColeta(coleta.tipoColeta))
            campoDuasColunas("Status", formatarStatus(coleta.status))
            campoDuasColunas("Data da coleta", formatarDataHora(coleta.dataColeta))
            campoDuasColunas("Município/UF", montarMunicipioUf(coleta))
        }
    }

    private fun desenharDadosGerais(
        escritor: EscritorPdf,
        coleta: ColetaEntity
    ) {
        escritor.secao("1. Dados gerais")

        escritor.tabelaCampos {
            linha("Município", coleta.municipio)
            linha("UF", coleta.uf)
            linha("Informante", coleta.informante)
            linha("Contato do informante", coleta.contatoInformante)
            linha("Latitude", coleta.latitude?.toString().orEmpty())
            linha("Longitude", coleta.longitude?.toString().orEmpty())

            if (!coleta.observacao.isNullOrBlank()) {
                linha("Observação", coleta.observacao)
            }
        }
    }

    private fun desenharRespostas(
        escritor: EscritorPdf,
        variaveis: List<VariavelEntity>,
        respostas: List<RespostaColetaEntity>
    ) {
        escritor.secao("2. Variáveis respondidas")

        if (variaveis.isEmpty()) {
            escritor.aviso("Nenhuma variável vinculada ao formulário desta coleta.")
            return
        }

        val respostasPorVariavel = respostas.associateBy { resposta ->
            resposta.variavelId
        }

        escritor.tabelaCampos {
            variaveis.forEach { variavel ->
                val resposta = respostasPorVariavel[variavel.id]
                val nome = if (variavel.unidade.isNullOrBlank()) {
                    variavel.nome
                } else {
                    "${variavel.nome} (${variavel.unidade})"
                }

                linha(
                    rotulo = nome,
                    valor = formatarResposta(resposta)
                )
            }
        }
    }

    private fun desenharBenfeitorias(
        escritor: EscritorPdf,
        benfeitorias: List<BenfeitoriaEntity>
    ) {
        escritor.secao("3. Benfeitorias")

        if (benfeitorias.isEmpty()) {
            escritor.aviso("Nenhuma benfeitoria cadastrada nesta coleta.")
            return
        }

        benfeitorias.forEachIndexed { index, benfeitoria ->
            escritor.cardBenfeitoria(
                titulo = "${index + 1}. ${benfeitoria.nome.ifBlank { benfeitoria.categoria.ifBlank { "Benfeitoria" } }}"
            ) {
                linha("Categoria", benfeitoria.categoria)
                linha("Nome", benfeitoria.nome)
                linha("Descrição", benfeitoria.descricao.orEmpty())
                linha("Quantidade", formatarQuantidade(benfeitoria))
                linha("Estado de conservação", benfeitoria.estadoConservacao.orEmpty())
                linha("Idade aproximada", benfeitoria.idadeAproximada?.let { "$it anos" }.orEmpty())
                linha("Observação", benfeitoria.observacao.orEmpty())
            }
        }
    }

    private fun desenharFotosGerais(
        context: Context,
        escritor: EscritorPdf,
        fotos: List<FotoColetaEntity>
    ) {
        escritor.secao("4. Fotos gerais")

        if (fotos.isEmpty()) {
            escritor.aviso("Nenhuma foto geral cadastrada nesta coleta.")
            return
        }

        escritor.gradeFotos(
            context = context,
            fotos = fotos.mapIndexed { index, foto ->
                FotoPdfItem(
                    caminho = foto.caminhoArquivo,
                    titulo = "Foto geral ${index + 1}",
                    legenda = montarLegendaFoto(
                        legenda = foto.legenda,
                        latitude = foto.latitude,
                        longitude = foto.longitude,
                        dataHora = foto.dataHora
                    )
                )
            }
        )
    }

    private fun desenharFotosDasBenfeitorias(
        context: Context,
        escritor: EscritorPdf,
        benfeitorias: List<BenfeitoriaEntity>,
        fotosPorBenfeitoria: Map<Long, List<FotoBenfeitoriaEntity>>
    ) {
        escritor.secao("5. Fotos das benfeitorias")

        if (benfeitorias.isEmpty()) {
            escritor.aviso("Nenhuma benfeitoria cadastrada para exibir fotos.")
            return
        }

        var possuiFoto = false

        benfeitorias.forEach { benfeitoria ->
            val fotos = fotosPorBenfeitoria[benfeitoria.id].orEmpty()

            if (fotos.isNotEmpty()) {
                possuiFoto = true

                escritor.subtituloBloco(
                    benfeitoria.nome.ifBlank {
                        benfeitoria.categoria.ifBlank { "Benfeitoria" }
                    }
                )

                escritor.gradeFotos(
                    context = context,
                    fotos = fotos.mapIndexed { index, foto ->
                        FotoPdfItem(
                            caminho = foto.caminhoArquivo,
                            titulo = "Foto ${index + 1}",
                            legenda = montarLegendaFoto(
                                legenda = foto.legenda,
                                latitude = foto.latitude,
                                longitude = foto.longitude,
                                dataHora = foto.dataHora
                            )
                        )
                    }
                )
            }
        }

        if (!possuiFoto) {
            escritor.aviso("Nenhuma foto de benfeitoria cadastrada nesta coleta.")
        }
    }

    private fun formatarResposta(
        resposta: RespostaColetaEntity?
    ): String {
        if (resposta == null) {
            return "Não informado"
        }

        return when {
            !resposta.valorTexto.isNullOrBlank() -> resposta.valorTexto
            resposta.valorNumero != null -> resposta.valorNumero.toString()
            resposta.valorBooleano != null -> if (resposta.valorBooleano) "Sim" else "Não"
            resposta.valorData != null -> formatarData(resposta.valorData)
            resposta.opcaoId != null -> "Opção ID ${resposta.opcaoId}"
            else -> "Não informado"
        }
    }

    private fun formatarQuantidade(
        benfeitoria: BenfeitoriaEntity
    ): String {
        if (benfeitoria.quantidade == null) {
            return ""
        }

        return listOfNotNull(
            benfeitoria.quantidade.toString(),
            benfeitoria.unidade?.takeIf { it.isNotBlank() }
        ).joinToString(" ")
    }

    private fun montarLegendaFoto(
        legenda: String?,
        latitude: Double?,
        longitude: Double?,
        dataHora: Long
    ): String {
        val partes = mutableListOf<String>()

        if (!legenda.isNullOrBlank()) {
            partes.add(legenda)
        }

        partes.add("Data/hora: ${formatarDataHora(dataHora)}")

        if (latitude != null && longitude != null) {
            partes.add("Coordenadas: $latitude, $longitude")
        }

        return partes.joinToString(" | ")
    }

    private fun montarMunicipioUf(
        coleta: ColetaEntity
    ): String {
        val municipio = coleta.municipio.ifBlank { "Não informado" }
        val uf = coleta.uf.ifBlank { "Não informado" }
        return "$municipio/$uf"
    }

    private fun formatarTipoColeta(
        valor: String
    ): String {
        return when (valor.uppercase()) {
            "AVALIANDO" -> "Imóvel avaliando"
            "AMOSTRAL" -> "Dado amostral"
            else -> valor.ifBlank { "Não informado" }
        }
    }

    private fun formatarStatus(
        valor: String
    ): String {
        return when (valor.uppercase()) {
            "RASCUNHO" -> "Rascunho"
            "CONCLUIDA" -> "Concluída"
            else -> valor.ifBlank { "Não informado" }
        }
    }

    private fun formatarDataHora(
        millis: Long
    ): String {
        return SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            Locale("pt", "BR")
        ).format(Date(millis))
    }

    private fun formatarData(
        millis: Long
    ): String {
        return SimpleDateFormat(
            "dd/MM/yyyy",
            Locale("pt", "BR")
        ).format(Date(millis))
    }

    private fun nomeSeguro(
        texto: String
    ): String {
        val semAcentos = Normalizer
            .normalize(texto, Normalizer.Form.NFD)
            .replace(Regex("\\p{Mn}+"), "")

        return semAcentos
            .trim()
            .ifBlank { "relatorio" }
            .lowercase()
            .replace(Regex("\\s+"), "_")
            .replace(Regex("[^a-z0-9_-]"), "_")
            .replace(Regex("_+"), "_")
            .take(70)
    }

    private fun carregarLogoRuralColeta(
        context: Context
    ): Bitmap? {
        return try {
            val drawable = ContextCompat.getDrawable(
                context,
                R.mipmap.ic_logo
            ) ?: return null

            val largura = 192
            val altura = 192

            val bitmap = Bitmap.createBitmap(
                largura,
                altura,
                Bitmap.Config.ARGB_8888
            )

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, largura, altura)
            drawable.draw(canvas)

            bitmap
        } catch (_: Exception) {
            null
        }
    }

    private fun carregarBitmapReduzido(
        context: Context,
        caminho: String,
        larguraMaxima: Int = 1200,
        alturaMaxima: Int = 1200
    ): Bitmap? {
        return try {
            if (caminho.startsWith("content://")) {
                val bytes = context.contentResolver.openInputStream(Uri.parse(caminho))?.use { input ->
                    input.readBytes()
                } ?: return null

                val bounds = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }

                BitmapFactory.decodeStream(
                    ByteArrayInputStream(bytes),
                    null,
                    bounds
                )

                val options = BitmapFactory.Options().apply {
                    inSampleSize = calcularInSampleSize(
                        options = bounds,
                        larguraMaxima = larguraMaxima,
                        alturaMaxima = alturaMaxima
                    )
                    inPreferredConfig = Bitmap.Config.RGB_565
                }

                BitmapFactory.decodeStream(
                    ByteArrayInputStream(bytes),
                    null,
                    options
                )
            } else {
                val arquivo = File(caminho)

                if (!arquivo.exists()) {
                    return null
                }

                val bounds = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }

                BitmapFactory.decodeFile(
                    arquivo.absolutePath,
                    bounds
                )

                val options = BitmapFactory.Options().apply {
                    inSampleSize = calcularInSampleSize(
                        options = bounds,
                        larguraMaxima = larguraMaxima,
                        alturaMaxima = alturaMaxima
                    )
                    inPreferredConfig = Bitmap.Config.RGB_565
                }

                BitmapFactory.decodeFile(
                    arquivo.absolutePath,
                    options
                )
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun calcularInSampleSize(
        options: BitmapFactory.Options,
        larguraMaxima: Int,
        alturaMaxima: Int
    ): Int {
        val altura = options.outHeight
        val largura = options.outWidth
        var inSampleSize = 1

        if (altura > alturaMaxima || largura > larguraMaxima) {
            var metadeAltura = altura / 2
            var metadeLargura = largura / 2

            while (
                metadeAltura / inSampleSize >= alturaMaxima &&
                metadeLargura / inSampleSize >= larguraMaxima
            ) {
                inSampleSize *= 2
            }
        }

        return max(1, inSampleSize)
    }

    private data class FotoPdfItem(
        val caminho: String,
        val titulo: String,
        val legenda: String
    )

    private inner class EscritorPdf(
        private val documento: PdfDocument
    ) {
        private var paginaAtual: PdfDocument.Page? = null
        private var canvas: Canvas? = null
        private var numeroPagina = 0
        private var y = margem
        private val dataGeracao = SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            Locale("pt", "BR")
        ).format(Date())

        private val verdeEscuro = Color.rgb(0, 72, 42)
        private val verde = Color.rgb(0, 128, 72)
        private val verdeClaro = Color.rgb(232, 245, 238)
        private val cinzaTexto = Color.rgb(45, 45, 45)
        private val cinzaSecundario = Color.rgb(105, 105, 105)
        private val cinzaLinha = Color.rgb(222, 226, 223)
        private val cinzaFundo = Color.rgb(248, 250, 249)
        private val branco = Color.WHITE

        private val paintTituloPrincipal = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = branco
            textSize = 20f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        private val paintTituloSecundario = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(225, 244, 234)
            textSize = 10.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        private val paintCabecalhoNome = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = branco
            textSize = 13.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        private val paintMarca = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = verdeEscuro
            textSize = 15f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        private val paintLogoFundo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = branco
            style = Paint.Style.FILL
        }

        private val paintLogoBorda = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(210, 232, 220)
            style = Paint.Style.STROKE
            strokeWidth = 1.4f
        }

        private val paintSecao = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = verdeEscuro
            textSize = 13.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        private val paintSubtitulo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(35, 35, 35)
            textSize = 12f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        private val paintRotulo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(80, 80, 80)
            textSize = 9.7f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        private val paintTexto = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = cinzaTexto
            textSize = 10.2f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        private val paintTextoPequeno = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = cinzaSecundario
            textSize = 8.7f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        private val paintAviso = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(95, 95, 95)
            textSize = 10.2f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        }

        private val paintLinha = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = cinzaLinha
            strokeWidth = 1f
        }

        private val paintBorda = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = cinzaLinha
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }

        private val paintCardFundo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = cinzaFundo
            style = Paint.Style.FILL
        }

        private val paintCardDestaque = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = verdeClaro
            style = Paint.Style.FILL
        }

        private val paintVerde = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = verde
            style = Paint.Style.FILL
        }

        private val paintVerdeEscuro = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = verdeEscuro
            style = Paint.Style.FILL
        }

        fun novaPagina() {
            fecharPaginaAtual()

            numeroPagina++

            val pageInfo = PdfDocument.PageInfo.Builder(
                larguraPagina,
                alturaPagina,
                numeroPagina
            ).create()

            paginaAtual = documento.startPage(pageInfo)
            canvas = paginaAtual?.canvas
            y = margem
            desenharFundoPagina()
        }

        fun finalizar() {
            fecharPaginaAtual()
        }

        fun cabecalhoRelatorio(
            context: Context,
            titulo: String,
            subtitulo: String,
            nomeReferencia: String,
            tipo: String,
            status: String
        ) {
            garantirEspaco(132f)

            val topo = y
            val altura = 118f
            val rect = RectF(
                margem,
                topo,
                larguraPagina - margem,
                topo + altura
            )

            canvas?.drawRoundRect(
                rect,
                14f,
                14f,
                paintVerdeEscuro
            )

            desenharMarcaRuralColeta(
                context = context,
                xCentro = margem + 33f,
                yCentro = topo + 34f,
                tamanho = 42f
            )

            canvas?.drawText(
                titulo.uppercase(),
                margem + 66f,
                topo + 32f,
                paintTituloPrincipal
            )

            canvas?.drawText(
                "RuralColeta • $subtitulo",
                margem + 66f,
                topo + 51f,
                paintTituloSecundario
            )

            val nome = nomeReferencia.ifBlank { "Não informado" }
            desenharTextoLimitado(
                texto = nome,
                paint = paintCabecalhoNome,
                x = margem + 18f,
                yTexto = topo + 82f,
                larguraMaxima = larguraConteudo - 36f
            )

            chip(
                texto = tipo,
                x = margem + 18f,
                yTopo = topo + 94f
            )

            chip(
                texto = status,
                x = margem + 168f,
                yTopo = topo + 94f
            )

            y += altura + 16f
        }

        fun cardResumo(
            titulo: String,
            conteudo: EscritorPdf.() -> Unit
        ) {
            garantirEspaco(130f)
            camposDuasColunasContador = 0

            val yInicio = y
            y += 20f

            canvas?.drawText(
                titulo,
                margem + 14f,
                y,
                paintSecao
            )

            y += 16f
            conteudo()
            y += 8f

            val rect = RectF(
                margem,
                yInicio,
                larguraPagina - margem,
                y
            )

            canvas?.drawRoundRect(
                rect,
                10f,
                10f,
                paintCardDestaque
            )

            canvas?.drawRoundRect(
                rect,
                10f,
                10f,
                paintBorda
            )

            val yFim = y
            y = yInicio + 20f
            camposDuasColunasContador = 0

            canvas?.drawText(
                titulo,
                margem + 14f,
                y,
                paintSecao
            )

            y += 16f
            conteudo()
            y = yFim + 12f
        }

        fun campoDuasColunas(
            rotulo: String,
            valor: String
        ) {
            val indiceNaLinha = camposDuasColunasContador % 2
            val colunaLargura = (larguraConteudo - 28f) / 2f
            val x = margem + 14f + (indiceNaLinha * (colunaLargura + 14f))

            if (indiceNaLinha == 0) {
                garantirEspaco(35f)
            }

            val yRotulo = y
            canvas?.drawText(
                rotulo.uppercase(),
                x,
                yRotulo,
                paintRotulo
            )

            desenharTextoQuebradoEmPosicao(
                texto = valor.ifBlank { "Não informado" },
                paint = paintTexto,
                x = x,
                yTextoInicial = yRotulo + 14f,
                larguraMaxima = colunaLargura,
                alturaLinha = 12f,
                maxLinhas = 2
            )

            camposDuasColunasContador++

            if (indiceNaLinha == 1) {
                y += 35f
            }
        }

        private var camposDuasColunasContador = 0

        fun secao(texto: String) {
            camposDuasColunasContador = 0
            garantirEspaco(42f)
            y += 6f

            canvas?.drawRoundRect(
                RectF(
                    margem,
                    y - 10f,
                    margem + 5f,
                    y + 10f
                ),
                2f,
                2f,
                paintVerde
            )

            canvas?.drawText(
                texto,
                margem + 13f,
                y + 4f,
                paintSecao
            )

            y += 22f

            canvas?.drawLine(
                margem,
                y,
                larguraPagina - margem,
                y,
                paintLinha
            )

            y += 12f
        }

        fun subtituloBloco(texto: String) {
            garantirEspaco(30f)
            canvas?.drawText(
                texto,
                margem,
                y,
                paintSubtitulo
            )
            y += 16f
        }

        fun aviso(texto: String) {
            garantirEspaco(38f)

            val rect = RectF(
                margem,
                y - 4f,
                larguraPagina - margem,
                y + 28f
            )

            canvas?.drawRoundRect(
                rect,
                8f,
                8f,
                paintCardFundo
            )

            desenharTextoQuebrado(
                texto = texto,
                paint = paintAviso,
                x = margem + 12f,
                larguraMaxima = larguraConteudo - 24f,
                alturaLinha = 13f,
                garantirAntesDeCadaLinha = false
            )

            y += 8f
        }

        fun tabelaCampos(
            conteudo: TabelaCampos.() -> Unit
        ) {
            val tabela = TabelaCampos()
            tabela.conteudo()
            tabela.linhas.forEachIndexed { index, linha ->
                linhaTabela(
                    rotulo = linha.rotulo,
                    valor = linha.valor,
                    destacar = index % 2 == 0
                )
            }
            y += 8f
        }

        fun cardBenfeitoria(
            titulo: String,
            conteudo: TabelaCampos.() -> Unit
        ) {
            val tabela = TabelaCampos()
            tabela.conteudo()

            val linhasValidas = tabela.linhas.filter { linha ->
                linha.valor.isNotBlank()
            }

            val alturaPrevista = 40f + (linhasValidas.size * 23f)
            garantirEspaco(min(alturaPrevista, 180f))

            val yInicio = y
            y += 22f

            canvas?.drawText(
                titulo,
                margem + 12f,
                y,
                paintSubtitulo
            )

            y += 14f

            linhasValidas.forEachIndexed { index, linha ->
                linhaTabelaInternaCard(
                    rotulo = linha.rotulo,
                    valor = linha.valor,
                    destacar = index % 2 == 0
                )
            }

            y += 6f

            val rect = RectF(
                margem,
                yInicio,
                larguraPagina - margem,
                y
            )

            canvas?.drawRoundRect(
                rect,
                10f,
                10f,
                paintCardFundo
            )

            canvas?.drawRoundRect(
                rect,
                10f,
                10f,
                paintBorda
            )

            val yFim = y
            y = yInicio + 22f

            canvas?.drawText(
                titulo,
                margem + 12f,
                y,
                paintSubtitulo
            )

            y += 14f

            linhasValidas.forEachIndexed { index, linha ->
                linhaTabelaInternaCard(
                    rotulo = linha.rotulo,
                    valor = linha.valor,
                    destacar = index % 2 == 0
                )
            }

            y = yFim + 10f
        }

        fun gradeFotos(
            context: Context,
            fotos: List<FotoPdfItem>
        ) {
            val larguraColuna = (larguraConteudo - 12f) / 2f
            val alturaImagem = 145f
            val alturaCard = 208f

            fotos.chunked(2).forEach { linhaFotos ->
                garantirEspaco(alturaCard + 12f)

                val yLinha = y

                linhaFotos.forEachIndexed { index, foto ->
                    val x = margem + (index * (larguraColuna + 12f))
                    desenharCardFoto(
                        context = context,
                        foto = foto,
                        x = x,
                        yTopo = yLinha,
                        largura = larguraColuna,
                        alturaImagem = alturaImagem,
                        alturaCard = alturaCard
                    )
                }

                y += alturaCard + 14f
            }
        }


        private fun desenharMarcaRuralColeta(
            context: Context,
            xCentro: Float,
            yCentro: Float,
            tamanho: Float
        ) {
            val canvasAtual = canvas ?: return
            val metade = tamanho / 2f
            val rectLogo = RectF(
                xCentro - metade,
                yCentro - metade,
                xCentro + metade,
                yCentro + metade
            )

            canvasAtual.drawRoundRect(
                rectLogo,
                10f,
                10f,
                paintLogoFundo
            )

            val bitmapLogo = carregarLogoRuralColeta(context)

            if (bitmapLogo != null) {
                val padding = 2.5f
                val destinoLogo = RectF(
                    rectLogo.left + padding,
                    rectLogo.top + padding,
                    rectLogo.right - padding,
                    rectLogo.bottom - padding
                )

                desenharBitmapComCantosArredondados(
                    bitmap = bitmapLogo,
                    destino = destinoLogo,
                    raio = 8f
                )

                bitmapLogo.recycle()
            } else {
                canvasAtual.drawCircle(
                    xCentro,
                    yCentro,
                    metade - 2f,
                    paintVerde
                )

                canvasAtual.drawText(
                    "RC",
                    xCentro,
                    yCentro + 5f,
                    paintMarca
                )
            }

            canvasAtual.drawRoundRect(
                rectLogo,
                10f,
                10f,
                paintLogoBorda
            )
        }

        private fun desenharBitmapComCantosArredondados(
            bitmap: Bitmap,
            destino: RectF,
            raio: Float
        ) {
            val canvasAtual = canvas ?: return
            val path = Path().apply {
                addRoundRect(
                    destino,
                    raio,
                    raio,
                    Path.Direction.CW
                )
            }

            canvasAtual.save()
            canvasAtual.clipPath(path)
            canvasAtual.drawBitmap(
                bitmap,
                null,
                destino,
                null
            )
            canvasAtual.restore()
        }

        private fun desenharCardFoto(
            context: Context,
            foto: FotoPdfItem,
            x: Float,
            yTopo: Float,
            largura: Float,
            alturaImagem: Float,
            alturaCard: Float
        ) {
            val rectCard = RectF(
                x,
                yTopo,
                x + largura,
                yTopo + alturaCard
            )

            canvas?.drawRoundRect(
                rectCard,
                8f,
                8f,
                paintCardFundo
            )

            canvas?.drawRoundRect(
                rectCard,
                8f,
                8f,
                paintBorda
            )

            val bitmap = carregarBitmapReduzido(
                context = context,
                caminho = foto.caminho
            )

            val rectImagem = RectF(
                x + 8f,
                yTopo + 8f,
                x + largura - 8f,
                yTopo + 8f + alturaImagem
            )

            if (bitmap != null) {
                desenharBitmapCentralizado(
                    bitmap = bitmap,
                    destino = rectImagem
                )
                bitmap.recycle()
            } else {
                canvas?.drawRoundRect(
                    rectImagem,
                    6f,
                    6f,
                    Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = Color.rgb(235, 238, 236)
                        style = Paint.Style.FILL
                    }
                )

                desenharTextoCentralizado(
                    texto = "Foto não encontrada",
                    xCentro = rectImagem.centerX(),
                    yTexto = rectImagem.centerY(),
                    paint = paintTextoPequeno
                )
            }

            canvas?.drawText(
                foto.titulo,
                x + 8f,
                yTopo + alturaImagem + 27f,
                paintRotulo
            )

            desenharTextoQuebradoEmPosicao(
                texto = foto.legenda,
                paint = paintTextoPequeno,
                x = x + 8f,
                yTextoInicial = yTopo + alturaImagem + 42f,
                larguraMaxima = largura - 16f,
                alturaLinha = 10.5f,
                maxLinhas = 3
            )
        }

        private fun linhaTabela(
            rotulo: String,
            valor: String,
            destacar: Boolean
        ) {
            val valorFinal = valor.ifBlank { "Não informado" }
            val alturaMinima = 25f
            val yInicio = y

            garantirEspaco(alturaMinima + 8f)

            if (destacar) {
                canvas?.drawRoundRect(
                    RectF(
                        margem,
                        y - 9f,
                        larguraPagina - margem,
                        y + 13f
                    ),
                    5f,
                    5f,
                    paintCardFundo
                )
            }

            canvas?.drawText(
                rotulo,
                margem + 8f,
                y + 5f,
                paintRotulo
            )

            val xValor = margem + 178f
            val larguraValor = larguraConteudo - 188f

            val yDepois = desenharTextoQuebrado(
                texto = valorFinal,
                paint = paintTexto,
                x = xValor,
                larguraMaxima = larguraValor,
                alturaLinha = 13f,
                garantirAntesDeCadaLinha = false
            )

            y = max(yInicio + alturaMinima, yDepois + 2f)
        }

        private fun linhaTabelaInternaCard(
            rotulo: String,
            valor: String,
            destacar: Boolean
        ) {
            val yInicio = y

            if (destacar) {
                canvas?.drawRoundRect(
                    RectF(
                        margem + 8f,
                        y - 8f,
                        larguraPagina - margem - 8f,
                        y + 13f
                    ),
                    5f,
                    5f,
                    Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = Color.rgb(242, 246, 244)
                        style = Paint.Style.FILL
                    }
                )
            }

            canvas?.drawText(
                rotulo,
                margem + 16f,
                y + 5f,
                paintRotulo
            )

            val xValor = margem + 178f
            val larguraValor = larguraConteudo - 196f

            val yDepois = desenharTextoQuebrado(
                texto = valor.ifBlank { "Não informado" },
                paint = paintTexto,
                x = xValor,
                larguraMaxima = larguraValor,
                alturaLinha = 13f,
                garantirAntesDeCadaLinha = false
            )

            y = max(yInicio + 22f, yDepois + 2f)
        }

        private fun desenharFundoPagina() {
            canvas?.drawColor(branco)

            canvas?.drawRect(
                0f,
                0f,
                larguraPagina.toFloat(),
                8f,
                paintVerdeEscuro
            )
        }

        private fun garantirEspaco(
            alturaNecessaria: Float
        ) {
            if (paginaAtual == null) {
                novaPagina()
                return
            }

            val limite = alturaPagina - margem - rodapeAltura

            if (y + alturaNecessaria > limite) {
                novaPagina()
            }
        }

        private fun desenharBitmapCentralizado(
            bitmap: Bitmap,
            destino: RectF
        ) {
            val escala = min(
                destino.width() / bitmap.width,
                destino.height() / bitmap.height
            )

            val larguraImagem = bitmap.width * escala
            val alturaImagem = bitmap.height * escala
            val esquerda = destino.left + ((destino.width() - larguraImagem) / 2f)
            val topo = destino.top + ((destino.height() - alturaImagem) / 2f)

            val destinoFinal = RectF(
                esquerda,
                topo,
                esquerda + larguraImagem,
                topo + alturaImagem
            )

            canvas?.drawBitmap(
                bitmap,
                null,
                destinoFinal,
                null
            )
        }

        private fun desenharTextoQuebrado(
            texto: String,
            paint: Paint,
            x: Float,
            larguraMaxima: Float,
            alturaLinha: Float,
            garantirAntesDeCadaLinha: Boolean
        ): Float {
            val palavras = texto
                .replace("\n", " ")
                .split(Regex("\\s+"))
                .filter { it.isNotBlank() }

            if (palavras.isEmpty()) {
                if (garantirAntesDeCadaLinha) {
                    garantirEspaco(alturaLinha)
                }
                canvas?.drawText("", x, y, paint)
                return y + alturaLinha
            }

            var linha = ""

            palavras.forEach { palavra ->
                val candidata = if (linha.isBlank()) {
                    palavra
                } else {
                    "$linha $palavra"
                }

                if (paint.measureText(candidata) <= larguraMaxima) {
                    linha = candidata
                } else {
                    if (garantirAntesDeCadaLinha) {
                        garantirEspaco(alturaLinha)
                    }
                    canvas?.drawText(linha, x, y + 5f, paint)
                    y += alturaLinha
                    linha = palavra
                }
            }

            if (linha.isNotBlank()) {
                if (garantirAntesDeCadaLinha) {
                    garantirEspaco(alturaLinha)
                }
                canvas?.drawText(linha, x, y + 5f, paint)
                y += alturaLinha
            }

            return y
        }

        private fun desenharTextoQuebradoEmPosicao(
            texto: String,
            paint: Paint,
            x: Float,
            yTextoInicial: Float,
            larguraMaxima: Float,
            alturaLinha: Float,
            maxLinhas: Int
        ) {
            val palavras = texto
                .replace("\n", " ")
                .split(Regex("\\s+"))
                .filter { it.isNotBlank() }

            if (palavras.isEmpty()) {
                return
            }

            val linhas = mutableListOf<String>()
            var linha = ""

            palavras.forEach { palavra ->
                val candidata = if (linha.isBlank()) palavra else "$linha $palavra"

                if (paint.measureText(candidata) <= larguraMaxima) {
                    linha = candidata
                } else {
                    linhas.add(linha)
                    linha = palavra
                }
            }

            if (linha.isNotBlank()) {
                linhas.add(linha)
            }

            linhas.take(maxLinhas).forEachIndexed { index, textoLinha ->
                val textoFinal = if (index == maxLinhas - 1 && linhas.size > maxLinhas) {
                    "$textoLinha..."
                } else {
                    textoLinha
                }

                canvas?.drawText(
                    textoFinal,
                    x,
                    yTextoInicial + (index * alturaLinha),
                    paint
                )
            }
        }

        private fun desenharTextoLimitado(
            texto: String,
            paint: Paint,
            x: Float,
            yTexto: Float,
            larguraMaxima: Float
        ) {
            var textoFinal = texto

            while (paint.measureText(textoFinal) > larguraMaxima && textoFinal.length > 3) {
                textoFinal = textoFinal.dropLast(4) + "..."
            }

            canvas?.drawText(
                textoFinal,
                x,
                yTexto,
                paint
            )
        }

        private fun desenharTextoCentralizado(
            texto: String,
            xCentro: Float,
            yTexto: Float,
            paint: Paint
        ) {
            val larguraTexto = paint.measureText(texto)
            canvas?.drawText(
                texto,
                xCentro - (larguraTexto / 2f),
                yTexto,
                paint
            )
        }

        private fun chip(
            texto: String,
            x: Float,
            yTopo: Float
        ) {
            val largura = 132f
            val altura = 18f

            val rect = RectF(
                x,
                yTopo,
                x + largura,
                yTopo + altura
            )

            val paintChip = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(232, 245, 238)
                style = Paint.Style.FILL
            }

            val paintChipTexto = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = verdeEscuro
                textSize = 8.8f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }

            canvas?.drawRoundRect(
                rect,
                9f,
                9f,
                paintChip
            )

            desenharTextoLimitado(
                texto = texto,
                paint = paintChipTexto,
                x = x + 8f,
                yTexto = yTopo + 12.5f,
                larguraMaxima = largura - 16f
            )
        }

        private fun fecharPaginaAtual() {
            val pagina = paginaAtual ?: return
            val canvasAtual = canvas ?: return

            desenharRodape(canvasAtual)
            documento.finishPage(pagina)

            paginaAtual = null
            canvas = null
        }

        private fun desenharRodape(
            canvasAtual: Canvas
        ) {
            val paintRodape = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(110, 110, 110)
                textSize = 8.6f
            }

            val yLinha = alturaPagina - 44f
            val yTexto = alturaPagina - 25f
            val textoEsquerda = "RuralColeta • Relatório gerado em $dataGeracao"
            val textoDireita = "Página $numeroPagina"

            canvasAtual.drawLine(
                margem,
                yLinha,
                larguraPagina - margem,
                yLinha,
                paintLinha
            )

            canvasAtual.drawText(
                textoEsquerda,
                margem,
                yTexto,
                paintRodape
            )

            val larguraTextoDireita = paintRodape.measureText(textoDireita)

            canvasAtual.drawText(
                textoDireita,
                larguraPagina - margem - larguraTextoDireita,
                yTexto,
                paintRodape
            )
        }
    }

    private class TabelaCampos {
        val linhas = mutableListOf<LinhaTabela>()

        fun linha(
            rotulo: String,
            valor: String
        ) {
            linhas.add(
                LinhaTabela(
                    rotulo = rotulo,
                    valor = valor
                )
            )
        }
    }

    private data class LinhaTabela(
        val rotulo: String,
        val valor: String
    )
}
