package br.com.agrobox.ruralcoleta.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.agrobox.ruralcoleta.data.local.entity.VariavelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VariavelDao {

    @Query("SELECT * FROM variavel WHERE ativo = 1 ORDER BY nome")
    fun listarAtivas(): Flow<List<VariavelEntity>>

    @Query("SELECT * FROM variavel WHERE grupoId = :grupoId AND ativo = 1 ORDER BY nome")
    fun listarPorGrupo(grupoId: Long): Flow<List<VariavelEntity>>

    @Query("SELECT * FROM variavel WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Long): VariavelEntity?

    @Insert
    suspend fun inserir(variavel: VariavelEntity): Long

    @Update
    suspend fun atualizar(variavel: VariavelEntity)

    @Delete
    suspend fun excluir(variavel: VariavelEntity)
}
