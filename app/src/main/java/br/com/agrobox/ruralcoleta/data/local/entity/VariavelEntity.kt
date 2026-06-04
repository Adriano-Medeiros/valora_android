package br.com.agrobox.ruralcoleta.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "variavel")
data class VariavelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val tipoCampo: String,
    val unidade: String? = null,
    val grupoId: Long? = null,
    val obrigatoria: Boolean = false,
    val dica: String? = null,
    val ativo: Boolean = true
)