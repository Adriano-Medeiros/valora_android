package br.com.agrobox.ruralcoleta.util

import br.com.agrobox.ruralcoleta.BuildConfig


object AppInfo {

    val versaoNome: String
        get() = BuildConfig.VERSION_NAME

    val versaoCodigo: Int
        get() = BuildConfig.VERSION_CODE

    val versaoFormatada: String
        get() = "Versão $versaoNome"
}