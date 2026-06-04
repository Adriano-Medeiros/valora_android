package br.com.agrobox.ruralcoleta.data.local.dao

import androidx.room.*
import br.com.agrobox.ruralcoleta.data.local.entity.FotoBenfeitoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FotoBenfeitoriaDao {

    @Query("SELECT * FROM foto_benfeitoria WHERE benfeitoriaId = :benfeitoriaId ORDER BY dataHora DESC")
    fun listarPorBenfeitoria(benfeitoriaId: Long): Flow<List<FotoBenfeitoriaEntity>>

    @Insert
    suspend fun inserir(foto: FotoBenfeitoriaEntity): Long

    @Update
    suspend fun atualizar(foto: FotoBenfeitoriaEntity)

    @Delete
    suspend fun excluir(foto: FotoBenfeitoriaEntity)
}