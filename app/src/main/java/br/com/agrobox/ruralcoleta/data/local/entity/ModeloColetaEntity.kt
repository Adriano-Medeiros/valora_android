package br.com.agrobox.ruralcoleta.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "modelo_coleta")
data class ModeloColetaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val descricao: String? = null,
    val ativo: Boolean = true
)