package br.com.agrobox.ruralcoleta.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grupo_variavel")
data class GrupoVariavelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val descricao: String? = null,
    val ordem: Int = 0,
    val ativo: Boolean = true
)