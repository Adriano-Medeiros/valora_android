package br.com.agrobox.ruralcoleta.util

fun filtrarDecimal(
    valor: String,
    permitirNegativo: Boolean = false
): String {
    val resultado = StringBuilder()
    var possuiSeparador = false

    valor.forEach { caractere ->
        when {
            caractere.isDigit() -> {
                resultado.append(caractere)
            }

            caractere == ',' || caractere == '.' -> {
                if (!possuiSeparador) {
                    resultado.append(caractere)
                    possuiSeparador = true
                }
            }

            caractere == '-' && permitirNegativo && resultado.isEmpty() -> {
                resultado.append(caractere)
            }
        }
    }

    return resultado.toString()
}

fun textoParaDouble(
    valor: String
): Double? {
    return valor
        .replace(",", ".")
        .toDoubleOrNull()
}