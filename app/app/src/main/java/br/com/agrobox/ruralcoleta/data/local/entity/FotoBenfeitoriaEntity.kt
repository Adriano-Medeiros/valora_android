package br.com.agrobox.ruralcoleta.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "foto_benfeitoria",
    foreignKeys = [
        ForeignKey(
            entity = BenfeitoriaEntity::class,
            parentColumns = ["id"],
            childColumns = ["benfeitoriaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("benfeitoriaId")]
)
data class FotoBenfeitoriaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val benfeitoriaId: Long,
    val caminhoArquivo: String,
    val legenda: String? = null,
    val observacao: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val dataHora: Long = System.currentTimeMillis()
)
