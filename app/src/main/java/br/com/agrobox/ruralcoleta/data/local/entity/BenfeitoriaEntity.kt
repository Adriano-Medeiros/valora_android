package br.com.agrobox.ruralcoleta.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "benfeitoria",
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
data class BenfeitoriaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val coletaId: Long,
    val categoria: String,
    val nome: String,
    val descricao: String? = null,
    val quantidade: Double? = null,
    val unidade: String? = null,
    val estadoConservacao: String? = null,
    val idadeAproximada: Int? = null,
    val observacao: String? = null
)