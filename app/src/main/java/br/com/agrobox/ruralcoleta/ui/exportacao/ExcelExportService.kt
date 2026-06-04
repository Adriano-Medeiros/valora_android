package br.com.agrobox.ruralcoleta.data.export

import android.content.Context
import br.com.agrobox.ruralcoleta.data.local.entity.ColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.RespostaColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.VariavelEntity
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ExcelExportService {

    fun exportarColetas(
        context: Context,
        nomeModelo: String,
        coletas: List<ColetaEntity>,
        variaveis: List<VariavelEntity>,
        respostasPorColeta: Map<Long, List<RespostaColetaEntity>>
    ): File {
        val pasta = File(context.filesDir, "exportacoes")

        if (!pasta.exists()) {
            pasta.mkdirs()
        }

        val arquivo = File(
            pasta,
            "exportacao_${normalizarNomeArquivo(nomeModelo)}_${System.currentTimeMillis()}.xlsx"
        )

        ZipOutputStream(FileOutputStream(arquivo)).use { zip ->
            adicionarArquivo(zip, "[Content_Types].xml", contentTypes())
            adicionarArquivo(zip, "_rels/.rels", rels())
            adicionarArquivo(zip, "xl/workbook.xml", workbook())
            adicionarArquivo(zip, "xl/_rels/workbook.xml.rels", workbookRels())
            adicionarArquivo(zip, "xl/styles.xml", styles())
            adicionarArquivo(
                zip,
                "xl/worksheets/sheet1.xml",
                sheetXml(
                    coletas = coletas,
                    variaveis = variaveis,
                    respostasPorColeta = respostasPorColeta
                )
            )
        }

        return arquivo
    }

    private fun sheetXml(
        coletas: List<ColetaEntity>,
        variaveis: List<VariavelEntity>,
        respostasPorColeta: Map<Long, List<RespostaColetaEntity>>
    ): String {
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

        val cabecalhos = colunasFixas + variaveis.map { it.nome }

        val linhas = StringBuilder()

        linhas.append("<row r=\"1\">")

        cabecalhos.forEachIndexed { index, titulo ->
            linhas.append(
                cell(
                    col = index + 1,
                    row = 1,
                    value = titulo,
                    style = 1
                )
            )
        }

        linhas.append("</row>")

        coletas.forEachIndexed { rowIndex, coleta ->
            val rowNumber = rowIndex + 2
            val respostas = respostasPorColeta[coleta.id].orEmpty()

            val valoresFixos = listOf(
                (rowIndex + 1).toString(),
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

            linhas.append("<row r=\"$rowNumber\">")

            valores.forEachIndexed { colIndex, valor ->
                linhas.append(
                    cell(
                        col = colIndex + 1,
                        row = rowNumber,
                        value = valor
                    )
                )
            }

            linhas.append("</row>")
        }

        return """
<worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main"
    xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">
    <sheetData>
        $linhas
    </sheetData>
</worksheet>
        """.trim()
    }

    private fun formatarResposta(
        resposta: RespostaColetaEntity?
    ): String {
        if (resposta == null) return ""

        return when {
            !resposta.valorTexto.isNullOrBlank() -> resposta.valorTexto
            resposta.valorNumero != null -> resposta.valorNumero.toString()
            resposta.valorBooleano != null -> if (resposta.valorBooleano) "Sim" else "Não"
            resposta.valorData != null -> resposta.valorData.toString()
            resposta.opcaoId != null -> resposta.opcaoId.toString()
            else -> ""
        }
    }

    private fun cell(
        col: Int,
        row: Int,
        value: String,
        style: Int = 0
    ): String {
        val ref = colunaExcel(col) + row

        return """
<c r="$ref" t="inlineStr" s="$style">
    <is>
        <t>${escapeXml(value)}</t>
    </is>
</c>
        """.trim()
    }

    private fun colunaExcel(
        index: Int
    ): String {
        var num = index
        var col = ""

        while (num > 0) {
            val rem = (num - 1) % 26
            col = ('A' + rem) + col
            num = (num - 1) / 26
        }

        return col
    }

    private fun escapeXml(
        text: String
    ): String {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }

    private fun adicionarArquivo(
        zip: ZipOutputStream,
        path: String,
        content: String
    ) {
        zip.putNextEntry(ZipEntry(path))

        zip.write(
            content.trim().toByteArray(Charsets.UTF_8)
        )

        zip.closeEntry()
    }

    private fun normalizarNomeArquivo(
        texto: String
    ): String {
        return texto
            .lowercase()
            .replace(" ", "_")
            .replace(Regex("[^a-z0-9_]"), "")
    }

    private fun contentTypes(): String {
        return """
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
    <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
    <Default Extension="xml" ContentType="application/xml"/>
    <Override PartName="/xl/workbook.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"/>
    <Override PartName="/xl/worksheets/sheet1.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml"/>
    <Override PartName="/xl/styles.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml"/>
</Types>
        """.trim()
    }

    private fun rels(): String {
        return """
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
    <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="xl/workbook.xml"/>
</Relationships>
        """.trim()
    }

    private fun workbook(): String {
        return """
<workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main"
    xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">
    <sheets>
        <sheet name="Coletas" sheetId="1" r:id="rId1"/>
    </sheets>
</workbook>
        """.trim()
    }

    private fun workbookRels(): String {
        return """
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
    <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet" Target="worksheets/sheet1.xml"/>
    <Relationship Id="rId2" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/>
</Relationships>
        """.trim()
    }

    private fun styles(): String {
        return """
<styleSheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">
    <fonts count="2">
        <font>
            <sz val="11"/>
            <name val="Calibri"/>
        </font>
        <font>
            <b/>
            <sz val="11"/>
            <name val="Calibri"/>
        </font>
    </fonts>
    <fills count="2">
        <fill>
            <patternFill patternType="none"/>
        </fill>
        <fill>
            <patternFill patternType="gray125"/>
        </fill>
    </fills>
    <borders count="1">
        <border/>
    </borders>
    <cellStyleXfs count="1">
        <xf numFmtId="0" fontId="0" fillId="0" borderId="0"/>
    </cellStyleXfs>
    <cellXfs count="2">
        <xf numFmtId="0" fontId="0" fillId="0" borderId="0" xfId="0"/>
        <xf numFmtId="0" fontId="1" fillId="0" borderId="0" xfId="0"/>
    </cellXfs>
</styleSheet>
        """.trim()
    }
}