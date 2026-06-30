package br.com.agrobox.ruralcoleta.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "foto_coleta",
    foreignKeys = [
        ForeignKey(
            entity = ColetaEntity::class,
            parentColumns = ["id"],
            childColumns = ["coletaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("coletaId")]
)
data class FotoColetaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val coletaId: Long,
    val caminhoArquivo: String,
    val legenda: String? = null,
    val observacao: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val dataHora: Long = System.currentTimeMillis()
)
