package br.com.agrobox.ruralcoleta.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.agrobox.ruralcoleta.data.local.entity.GrupoVariavelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GrupoVariavelDao {

    @Query("SELECT * FROM grupo_variavel WHERE ativo = 1 ORDER BY ordem, nome")
    fun listarAtivos(): Flow<List<GrupoVariavelEntity>>

    @Query("SELECT * FROM grupo_variavel WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Long): GrupoVariavelEntity?

    @Insert
    suspend fun inserir(grupo: GrupoVariavelEntity): Long

    @Update
    suspend fun atualizar(grupo: GrupoVariavelEntity)

    @Delete
    suspend fun excluir(grupo: GrupoVariavelEntity)
}
