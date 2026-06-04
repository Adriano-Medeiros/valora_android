package br.com.agrobox.ruralcoleta.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "resposta_coleta",
    foreignKeys = [
        ForeignKey(
            entity = ColetaEntity::class,
            parentColumns = ["id"],
            childColumns = ["coletaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = VariavelEntity::class,
            parentColumns = ["id"],
            childColumns = ["variavelId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = OpcaoVariavelEntity::class,
            parentColumns = ["id"],
            childColumns = ["opcaoId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("coletaId"),
        Index("variavelId"),
        Index("opcaoId")
    ]
)
data class RespostaColetaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val coletaId: Long,
    val variavelId: Long,
    val valorTexto: String? = null,
    val valorNumero: Double? = null,
    val valorBooleano: Boolean? = null,
    val valorData: Long? = null,
    val opcaoId: Long? = null
)