package br.com.agrobox.ruralcoleta.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "modelo_coleta_variavel",
    foreignKeys = [
        ForeignKey(
            entity = ModeloColetaEntity::class,
            parentColumns = ["id"],
            childColumns = ["modeloColetaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = VariavelEntity::class,
            parentColumns = ["id"],
            childColumns = ["variavelId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("modeloColetaId"),
        Index("variavelId")
    ]
)
data class ModeloColetaVariavelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val modeloColetaId: Long,
    val variavelId: Long,
    val ordem: Int = 0,
    val obrigatoriaNoModelo: Boolean = false
)