package br.com.agrobox.ruralcoleta.util

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileHelper {

    fun criarArquivoImagemColeta(
        context: Context,
        coletaId: Long
    ): File {

        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())

        val storageDir = File(
            context.filesDir,
            "coletas/$coletaId/fotos"
        )

        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        return File(
            storageDir,
            "IMG_${timeStamp}.jpg"
        )
    }
    fun criarArquivoImagemBenfeitoria(
        context: Context,
        benfeitoriaId: Long
    ): File {

        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())

        val storageDir = File(
            context.filesDir,
            "benfeitorias/$benfeitoriaId/fotos"
        )

        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        return File(
            storageDir,
            "IMG_${timeStamp}.jpg"
        )
    }
}