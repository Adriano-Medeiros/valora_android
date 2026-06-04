package br.com.agrobox.ruralcoleta.data.local.dao

import androidx.room.*
import br.com.agrobox.ruralcoleta.data.local.entity.FotoColetaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FotoColetaDao {

    @Query("SELECT * FROM foto_coleta WHERE coletaId = :coletaId ORDER BY dataHora DESC")
    fun listarPorColeta(coletaId: Long): Flow<List<FotoColetaEntity>>

    @Insert
    suspend fun inserir(foto: FotoColetaEntity): Long

    @Update
    suspend fun atualizar(foto: FotoColetaEntity)

    @Delete
    suspend fun excluir(foto: FotoColetaEntity)
}