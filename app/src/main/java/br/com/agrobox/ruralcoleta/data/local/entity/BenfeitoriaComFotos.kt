package br.com.agrobox.ruralcoleta.data.local.entity

data class BenfeitoriaComFotos(
    val id: Long,
    val coletaId: Long,
    val categoria: String,
    val nome: String,
    val descricao: String?,
    val quantidade: Double?,
    val unidade: String?,
    val estadoConservacao: String?,
    val idadeAproximada: Int?,
    val observacao: String?,
    val totalFotos: Int
)