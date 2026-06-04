package br.com.agrobox.ruralcoleta.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coleta")
data class ColetaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val tipoColeta: String,

    val modeloColetaId: Long? = null,

    val nomeReferencia: String,

    val municipio: String,

    val uf: String,

    val latitude: Double? = null,

    val longitude: Double? = null,

    val dataColeta: Long = System.currentTimeMillis(),

    val informante: String,

    val contatoInformante: String,

    val observacao: String? = null,

    val status: String = StatusColeta.RASCUNHO.name
)