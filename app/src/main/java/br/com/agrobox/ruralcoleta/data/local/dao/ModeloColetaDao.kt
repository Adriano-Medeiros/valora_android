package br.com.agrobox.ruralcoleta.data.local.dao

import androidx.room.*
import br.com.agrobox.ruralcoleta.data.local.entity.ModeloColetaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ModeloColetaDao {

    @Query("SELECT * FROM modelo_coleta WHERE ativo = 1 ORDER BY nome")
    fun listarAtivos(): Flow<List<ModeloColetaEntity>>

    @Query("SELECT * FROM modelo_coleta WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Long): ModeloColetaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(modelo: ModeloColetaEntity): Long

    @Update
    suspend fun atualizar(modelo: ModeloColetaEntity)

    @Delete
    suspend fun excluir(modelo: ModeloColetaEntity)
}