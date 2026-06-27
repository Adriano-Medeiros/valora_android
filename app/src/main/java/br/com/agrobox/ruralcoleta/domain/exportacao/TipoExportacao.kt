package br.com.agrobox.ruralcoleta.domain.exportacao

enum class TipoExportacao(
    val titulo: String
) {
    EXCEL("Excel"),
    ZIP("Pacote completo ZIP"),
    PDF("Relatório PDF")
}
