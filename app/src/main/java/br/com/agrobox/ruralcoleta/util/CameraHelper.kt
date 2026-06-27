package br.com.agrobox.ruralcoleta.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object CameraHelper {

    fun criarUriImagem(
        context: Context,
        file: File
    ): Uri {

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}