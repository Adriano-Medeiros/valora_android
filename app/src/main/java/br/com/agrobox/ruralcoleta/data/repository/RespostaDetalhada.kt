package br.com.agrobox.ruralcoleta.data.local.entity

data class RespostaDetalhada(
    val respostaId: Long,
    val coletaId: Long,
    val variavelId: Long,
    val nomeVariavel: String,
    val tipoCampo: String,
    val unidade: String?,
    val valorTexto: String?,
    val valorNumero: Double?,
    val valorBooleano: Boolean?,
    val valorData: Long?,
    val opcaoId: Long?
)