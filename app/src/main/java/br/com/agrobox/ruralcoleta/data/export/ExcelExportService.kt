package br.com.agrobox.ruralcoleta.data.export

import android.content.Context
import br.com.agrobox.ruralcoleta.data.local.entity.ColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.RespostaColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.VariavelEntity
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.ClientAnchor
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

class ExcelExportService {

    fun exportarColetas(
        context: Context,
        nomeModelo: String,
        coletas: List<ColetaEntity>,
        variaveis: List<VariavelEntity>,
        respostasPorColeta: Map<Long, List<RespostaColetaEntity>>,
        fotoPrincipalPorColeta: Map<Long, String?>,
        fotosBenfeitoriasPorColeta: Map<Long, List<String>>
    ): File {
        val pasta = File(context.filesDir, "exportacoes")

        if (!pasta.exists()) {
            pasta.mkdirs()
        }

        val arquivo = File(
            pasta,
            "exportacao_${normalizarNomeArquivo(nomeModelo)}_${System.currentTimeMillis()}.xlsx"
        )

        val workbook = XSSFWorkbook()

        criarPlanilha(
            workbook = workbook,
            coletas = coletas,
            variaveis = variaveis,
            respostasPorColeta = respostasPorColeta,
            fotoPrincipalPorColeta = fotoPrincipalPorColeta,
            fotosBenfeitoriasPorColeta = fotosBenfeitoriasPorColeta
        )

        FileOutputStream(arquivo).use { output ->
            workbook.write(output)
        }

        workbook.close()

        return arquivo
    }

    private fun criarPlanilha(
        workbook: Workbook,
        coletas: List<ColetaEntity>,
        variaveis: List<VariavelEntity>,
        respostasPorColeta: Map<Long, List<RespostaColetaEntity>>,
        fotoPrincipalPorColeta: Map<Long, String?>,
        fotosBenfeitoriasPorColeta: Map<Long, List<String>>
    ) {
        val sheet = workbook.createSheet("Coletas")
        val drawing = sheet.createDrawingPatriarch()
        val helper = workbook.creationHelper

        val headerStyle = workbook.createCellStyle().apply {
            fillForegroundColor = IndexedColors.LIGHT_GREEN.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            alignment = HorizontalAlignment.CENTER
            borderBottom = BorderStyle.THIN
            borderTop = BorderStyle.THIN
            borderLeft = BorderStyle.THIN
            borderRight = BorderStyle.THIN
        }

        val headerFont = workbook.createFont().apply {
            bold = true
            color = IndexedColors.BLACK.index
        }

        headerStyle.setFont(headerFont)

        val textStyle = workbook.createCellStyle().apply {
            borderBottom = BorderStyle.THIN
            borderTop = BorderStyle.THIN
            borderLeft = BorderStyle.THIN
            borderRight = BorderStyle.THIN
        }

        val maxFotosBenfeitorias = fotosBenfeitoriasPorColeta.values
            .maxOfOrNull { it.size }
            ?: 0

        val colunasFixas = listOf(
            "Ordem",
            "Nome do imóvel",
            "Município",
            "UF",
            "Informante",
            "Contato do informante",
            "Latitude",
            "Longitude",
            "Status"
        )

        val colunasVariaveis = variaveis.map { variavel ->
            if (variavel.unidade.isNullOrBlank()) {
                variavel.nome
            } else {
                "${variavel.nome} (${variavel.unidade})"
            }
        }

        val colunasFotos = listOf("Foto principal") +
                (1..maxFotosBenfeitorias).map { "Foto benfeitoria $it" }

        val cabecalhos = colunasFixas + colunasVariaveis + colunasFotos

        val headerRow = sheet.createRow(0)

        cabecalhos.forEachIndexed { index, titulo ->
            val cell = headerRow.createCell(index)
            cell.setCellValue(titulo)
            cell.cellStyle = headerStyle
        }

        coletas.forEachIndexed { index, coleta ->
            val rowIndex = index + 1
            val row = sheet.createRow(rowIndex)

            row.heightInPoints = 95f

            val respostas = respostasPorColeta[coleta.id].orEmpty()

            val valoresFixos = listOf(
                (index + 1).toString(),
                coleta.nomeReferencia,
                coleta.municipio,
                coleta.uf,
                coleta.informante,
                coleta.contatoInformante,
                coleta.latitude?.toString().orEmpty(),
                coleta.longitude?.toString().orEmpty(),
                if (coleta.status == "RASCUNHO") "Rascunho" else "Concluída"
            )

            val valoresVariaveis = variaveis.map { variavel ->
                val resposta = respostas.firstOrNull {
                    it.variavelId == variavel.id
                }

                formatarResposta(resposta)
            }

            val valores = valoresFixos + valoresVariaveis

            valores.forEachIndexed { colIndex, valor ->
                val cell = row.createCell(colIndex)
                cell.setCellValue(valor)
                cell.cellStyle = textStyle
            }

            val colunaFotoPrincipal = valores.size

            criarCelulaVaziaComBorda(
                row = row,
                colIndex = colunaFotoPrincipal,
                style = textStyle
            )

            inserirImagemNaCelula(
                workbook = workbook,
                drawing = drawing,
                helper = helper,
                caminhoImagem = fotoPrincipalPorColeta[coleta.id],
                rowIndex = rowIndex,
                colIndex = colunaFotoPrincipal
            )

            val fotosBenfeitorias = fotosBenfeitoriasPorColeta[coleta.id].orEmpty()

            (0 until maxFotosBenfeitorias).forEach { fotoIndex ->
                val colIndex = colunaFotoPrincipal + 1 + fotoIndex

                criarCelulaVaziaComBorda(
                    row = row,
                    colIndex = colIndex,
                    style = textStyle
                )

                inserirImagemNaCelula(
                    workbook = workbook,
                    drawing = drawing,
                    helper = helper,
                    caminhoImagem = fotosBenfeitorias.getOrNull(fotoIndex),
                    rowIndex = rowIndex,
                    colIndex = colIndex
                )
            }
        }

        sheet.createFreezePane(0, 1)

        cabecalhos.forEachIndexed { index, _ ->
            sheet.setColumnWidth(index, 22 * 256)
        }

        sheet.setColumnWidth(0, 10 * 256)
        sheet.setColumnWidth(1, 30 * 256)
        sheet.setColumnWidth(4, 26 * 256)
        sheet.setColumnWidth(5, 24 * 256)

        val primeiraColunaFoto = colunasFixas.size + colunasVariaveis.size

        (primeiraColunaFoto until cabecalhos.size).forEach { col ->
            sheet.setColumnWidth(col, 18 * 256)
        }
    }

    private fun criarCelulaVaziaComBorda(
        row: org.apache.poi.ss.usermodel.Row,
        colIndex: Int,
        style: org.apache.poi.ss.usermodel.CellStyle
    ) {
        val cell = row.createCell(colIndex)
        cell.setCellValue("")
        cell.cellStyle = style
    }

    private fun inserirImagemNaCelula(
        workbook: Workbook,
        drawing: org.apache.poi.ss.usermodel.Drawing<*>,
        helper: org.apache.poi.ss.usermodel.CreationHelper,
        caminhoImagem: String?,
        rowIndex: Int,
        colIndex: Int
    ) {
        if (caminhoImagem.isNullOrBlank()) {
            return
        }

        val arquivo = File(caminhoImagem)

        if (!arquivo.exists()) {
            return
        }

        val bytes = arquivo.readBytes()

        val tipoImagem = when (arquivo.extension.lowercase()) {
            "png" -> Workbook.PICTURE_TYPE_PNG
            else -> Workbook.PICTURE_TYPE_JPEG
        }

        val pictureIndex = workbook.addPicture(
            bytes,
            tipoImagem
        )

        val anchor = helper.createClientAnchor()

        anchor.setCol1(colIndex)
        anchor.setRow1(rowIndex)
        anchor.setCol2(colIndex + 1)
        anchor.setRow2(rowIndex + 1)

        drawing.createPicture(
            anchor,
            pictureIndex
        )
    }

    private fun formatarResposta(
        resposta: RespostaColetaEntity?
    ): String {
        if (resposta == null) {
            return ""
        }

        return when {
            !resposta.valorTexto.isNullOrBlank() -> resposta.valorTexto
            resposta.valorNumero != null -> resposta.valorNumero.toString()
            resposta.valorBooleano != null -> if (resposta.valorBooleano) "Sim" else "Não"
            resposta.valorData != null -> resposta.valorData.toString()
            resposta.opcaoId != null -> resposta.opcaoId.toString()
            else -> ""
        }
    }

    private fun normalizarNomeArquivo(
        texto: String
    ): String {
        return texto
            .lowercase()
            .replace(" ", "_")
            .replace(Regex("[^a-z0-9_]"), "")
    }
}
