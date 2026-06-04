package br.com.agrobox.ruralcoleta.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "opcao_variavel",
    foreignKeys = [
        ForeignKey(
            entity = VariavelEntity::class,
            parentColumns = ["id"],
            childColumns = ["variavelId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("variavelId")]
)
data class OpcaoVariavelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val variavelId: Long,
    val descricao: String,
    val ordem: Int = 0,
    val ativo: Boolean = true
)