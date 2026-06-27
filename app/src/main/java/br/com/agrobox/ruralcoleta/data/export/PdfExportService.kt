package br.com.agrobox.ruralcoleta.data.export

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
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
    private val rodapeAltura = 36f
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
            if (index == 0) {
                escritor.novaPagina()
            } else {
                escritor.novaPagina()
            }

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
        escritor.titulo("Relatório de Coleta Rural")

        if (totalColetas > 1) {
            escritor.subtitulo("Coleta $numeroColeta de $totalColetas")
        }

        escritor.espaco(8f)

        desenharResumoInicial(
            escritor = escritor,
            coleta = coleta
        )

        desenharDadosGerais(
            escritor = escritor,
            coleta = coleta,
            nomeFormulario = dados.modelo?.nome.orEmpty()
        )

        desenharRespostas(
            escritor = escritor,
            coleta = coleta,
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
        coleta: ColetaEntity
    ) {
        escritor.caixaDestaque {
            campo("Imóvel/referência", coleta.nomeReferencia)
            campo("Tipo da coleta", formatarTipoColeta(coleta.tipoColeta))
            campo("Status", formatarStatus(coleta.status))
            campo("Data da coleta", formatarDataHora(coleta.dataColeta))
        }
    }

    private fun desenharDadosGerais(
        escritor: EscritorPdf,
        coleta: ColetaEntity,
        nomeFormulario: String
    ) {
        escritor.secao("1. Dados gerais")

        escritor.campo("Formulário", nomeFormulario.ifBlank { "Não informado" })
        escritor.campo("Município", coleta.municipio)
        escritor.campo("UF", coleta.uf)
        escritor.campo("Informante", coleta.informante)
        escritor.campo("Contato do informante", coleta.contatoInformante)
        escritor.campo("Latitude", coleta.latitude?.toString().orEmpty())
        escritor.campo("Longitude", coleta.longitude?.toString().orEmpty())

        if (!coleta.observacao.isNullOrBlank()) {
            escritor.campo("Observação", coleta.observacao)
        }
    }

    private fun desenharRespostas(
        escritor: EscritorPdf,
        coleta: ColetaEntity,
        variaveis: List<VariavelEntity>,
        respostas: List<RespostaColetaEntity>
    ) {
        escritor.secao("2. Variáveis respondidas")

        if (variaveis.isEmpty()) {
            escritor.textoSecundario("Nenhuma variável vinculada ao formulário desta coleta.")
            return
        }

        val respostasPorVariavel = respostas.associateBy { resposta ->
            resposta.variavelId
        }

        variaveis.forEach { variavel ->
            val resposta = respostasPorVariavel[variavel.id]
            val nome = if (variavel.unidade.isNullOrBlank()) {
                variavel.nome
            } else {
                "${variavel.nome} (${variavel.unidade})"
            }

            escritor.campo(
                rotulo = nome,
                valor = formatarResposta(resposta)
            )
        }
    }

    private fun desenharBenfeitorias(
        escritor: EscritorPdf,
        benfeitorias: List<BenfeitoriaEntity>
    ) {
        escritor.secao("3. Benfeitorias")

        if (benfeitorias.isEmpty()) {
            escritor.textoSecundario("Nenhuma benfeitoria cadastrada nesta coleta.")
            return
        }

        benfeitorias.forEachIndexed { index, benfeitoria ->
            escritor.bloco(
                titulo = "${index + 1}. ${benfeitoria.nome.ifBlank { benfeitoria.categoria }}"
            ) {
                campo("Categoria", benfeitoria.categoria)
                campo("Nome", benfeitoria.nome)
                campo("Descrição", benfeitoria.descricao.orEmpty())
                campo("Quantidade", formatarQuantidade(benfeitoria))
                campo("Estado de conservação", benfeitoria.estadoConservacao.orEmpty())
                campo("Idade aproximada", benfeitoria.idadeAproximada?.let { "$it anos" }.orEmpty())
                campo("Observação", benfeitoria.observacao.orEmpty())
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
            escritor.textoSecundario("Nenhuma foto geral cadastrada nesta coleta.")
            return
        }

        fotos.forEachIndexed { index, foto ->
            escritor.foto(
                context = context,
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
    }

    private fun desenharFotosDasBenfeitorias(
        context: Context,
        escritor: EscritorPdf,
        benfeitorias: List<BenfeitoriaEntity>,
        fotosPorBenfeitoria: Map<Long, List<FotoBenfeitoriaEntity>>
    ) {
        escritor.secao("5. Fotos das benfeitorias")

        if (benfeitorias.isEmpty()) {
            escritor.textoSecundario("Nenhuma benfeitoria cadastrada para exibir fotos.")
            return
        }

        var possuiFoto = false

        benfeitorias.forEach { benfeitoria ->
            val fotos = fotosPorBenfeitoria[benfeitoria.id].orEmpty()

            if (fotos.isNotEmpty()) {
                possuiFoto = true
                escritor.subtitulo(benfeitoria.nome.ifBlank { benfeitoria.categoria })
            }

            fotos.forEachIndexed { index, foto ->
                escritor.foto(
                    context = context,
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
        }

        if (!possuiFoto) {
            escritor.textoSecundario("Nenhuma foto de benfeitoria cadastrada nesta coleta.")
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

    private fun formatarTipoColeta(
        valor: String
    ): String {
        return when (valor.uppercase()) {
            "AVALIANDO" -> "Imóvel avaliando"
            "AMOSTRAL" -> "Dado amostral"
            else -> valor
        }
    }

    private fun formatarStatus(
        valor: String
    ): String {
        return when (valor.uppercase()) {
            "RASCUNHO" -> "Rascunho"
            "CONCLUIDA" -> "Concluída"
            else -> valor
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

    private inner class EscritorPdf(
        private val documento: PdfDocument
    ) {
        private var paginaAtual: PdfDocument.Page? = null
        private var canvas: Canvas? = null
        private var numeroPagina = 0
        private var y = margem

        private val paintTitulo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(0, 96, 52)
            textSize = 20f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        private val paintSubtitulo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(40, 40, 40)
            textSize = 14f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        private val paintSecao = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 13f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        private val paintSecaoFundo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(0, 130, 59)
            style = Paint.Style.FILL
        }

        private val paintRotulo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(35, 35, 35)
            textSize = 10.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        private val paintTexto = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(45, 45, 45)
            textSize = 10.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        private val paintTextoSecundario = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(100, 100, 100)
            textSize = 10.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        }

        private val paintLinha = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(220, 220, 220)
            strokeWidth = 1f
        }

        private val paintCaixaFundo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(245, 250, 247)
            style = Paint.Style.FILL
        }

        private val paintCaixaBorda = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(205, 225, 215)
            style = Paint.Style.STROKE
            strokeWidth = 1f
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
        }

        fun finalizar() {
            fecharPaginaAtual()
        }

        fun titulo(texto: String) {
            garantirEspaco(36f)
            canvas?.drawText(
                texto,
                margem,
                y,
                paintTitulo
            )
            y += 24f
            linhaHorizontal()
            espaco(10f)
        }

        fun subtitulo(texto: String) {
            garantirEspaco(26f)
            canvas?.drawText(
                texto,
                margem,
                y,
                paintSubtitulo
            )
            y += 18f
        }

        fun secao(texto: String) {
            garantirEspaco(36f)
            espaco(8f)

            val rect = RectF(
                margem,
                y - 14f,
                larguraPagina - margem,
                y + 8f
            )

            canvas?.drawRoundRect(
                rect,
                6f,
                6f,
                paintSecaoFundo
            )

            canvas?.drawText(
                texto,
                margem + 8f,
                y + 1f,
                paintSecao
            )

            y += 22f
            espaco(6f)
        }

        fun campo(
            rotulo: String,
            valor: String
        ) {
            val valorFinal = valor.ifBlank { "Não informado" }
            garantirEspaco(32f)

            val yInicial = y
            canvas?.drawText(
                "$rotulo:",
                margem,
                y,
                paintRotulo
            )

            val xValor = margem + 150f
            val larguraValor = larguraConteudo - 150f
            val yFinal = desenharTextoQuebrado(
                texto = valorFinal,
                paint = paintTexto,
                x = xValor,
                larguraMaxima = larguraValor,
                alturaLinha = 14f,
                garantirAntesDeCadaLinha = false
            )

            y = max(yInicial + 16f, yFinal)
        }

        fun textoSecundario(texto: String) {
            garantirEspaco(28f)
            desenharTextoQuebrado(
                texto = texto,
                paint = paintTextoSecundario,
                x = margem,
                larguraMaxima = larguraConteudo,
                alturaLinha = 14f,
                garantirAntesDeCadaLinha = true
            )
            espaco(4f)
        }

        fun espaco(altura: Float) {
            garantirEspaco(altura)
            y += altura
        }

        fun linhaHorizontal() {
            canvas?.drawLine(
                margem,
                y,
                larguraPagina - margem,
                y,
                paintLinha
            )
            y += 8f
        }

        fun caixaDestaque(
            conteudo: EscritorPdf.() -> Unit
        ) {
            garantirEspaco(120f)

            val yInicio = y
            val yAntes = y

            y += 16f
            conteudo()
            y += 10f

            val rect = RectF(
                margem,
                yInicio,
                larguraPagina - margem,
                y
            )

            canvas?.drawRoundRect(
                rect,
                8f,
                8f,
                paintCaixaFundo
            )

            canvas?.drawRoundRect(
                rect,
                8f,
                8f,
                paintCaixaBorda
            )

            val yFim = y
            y = yAntes + 16f
            conteudo()
            y = yFim + 8f
        }

        fun bloco(
            titulo: String,
            conteudo: EscritorPdf.() -> Unit
        ) {
            garantirEspaco(80f)
            subtitulo(titulo)
            conteudo()
            espaco(6f)
            linhaHorizontal()
        }

        fun foto(
            context: Context,
            caminho: String,
            titulo: String,
            legenda: String
        ) {
            garantirEspaco(260f)

            subtitulo(titulo)

            val bitmap = carregarBitmapReduzido(
                context = context,
                caminho = caminho
            )

            if (bitmap == null) {
                textoSecundario("Foto não encontrada no caminho salvo.")
                return
            }

            val larguraMaximaImagem = larguraConteudo
            val alturaMaximaImagem = 230f

            if (y + alturaMaximaImagem + 40f > alturaPagina - margem - rodapeAltura) {
                novaPagina()
                subtitulo(titulo)
            }

            val escala = min(
                larguraMaximaImagem / bitmap.width,
                alturaMaximaImagem / bitmap.height
            )

            val larguraImagem = bitmap.width * escala
            val alturaImagem = bitmap.height * escala
            val esquerda = margem + ((larguraConteudo - larguraImagem) / 2f)
            val topo = y

            val destino = RectF(
                esquerda,
                topo,
                esquerda + larguraImagem,
                topo + alturaImagem
            )

            canvas?.drawBitmap(
                bitmap,
                null,
                destino,
                null
            )

            bitmap.recycle()

            y += alturaImagem + 10f

            desenharTextoQuebrado(
                texto = legenda,
                paint = paintTextoSecundario,
                x = margem,
                larguraMaxima = larguraConteudo,
                alturaLinha = 13f,
                garantirAntesDeCadaLinha = true
            )

            espaco(10f)
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
                    canvas?.drawText(linha, x, y, paint)
                    y += alturaLinha
                    linha = palavra
                }
            }

            if (linha.isNotBlank()) {
                if (garantirAntesDeCadaLinha) {
                    garantirEspaco(alturaLinha)
                }
                canvas?.drawText(linha, x, y, paint)
                y += alturaLinha
            }

            return y
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
                color = Color.rgb(120, 120, 120)
                textSize = 9f
            }

            val textoEsquerda = "RuralColeta"
            val textoDireita = "Página $numeroPagina"
            val yRodape = alturaPagina - 24f

            canvasAtual.drawLine(
                margem,
                alturaPagina - 42f,
                larguraPagina - margem,
                alturaPagina - 42f,
                paintLinha
            )

            canvasAtual.drawText(
                textoEsquerda,
                margem,
                yRodape,
                paintRodape
            )

            val larguraTextoDireita = paintRodape.measureText(textoDireita)

            canvasAtual.drawText(
                textoDireita,
                larguraPagina - margem - larguraTextoDireita,
                yRodape,
                paintRodape
            )
        }
    }
}
