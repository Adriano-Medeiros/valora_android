package br.com.agrobox.ruralcoleta.domain.exportacao

import java.io.File

data class ExportacaoResultado(
    val arquivo: File,
    val tipo: TipoExportacao,
    val mimeType: String,
    val tituloCompartilhamento: String
)
