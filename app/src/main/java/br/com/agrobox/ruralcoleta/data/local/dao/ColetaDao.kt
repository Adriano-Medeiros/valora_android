package br.com.agrobox.ruralcoleta.data.local.dao

import androidx.room.*
import br.com.agrobox.ruralcoleta.data.local.entity.ColetaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ColetaDao {

    @Query("SELECT * FROM coleta ORDER BY dataColeta DESC")
    fun listarTodas(): Flow<List<ColetaEntity>>

    @Query("SELECT * FROM coleta WHERE tipoColeta = :tipo ORDER BY dataColeta DESC")
    fun listarPorTipo(tipo: String): Flow<List<ColetaEntity>>

    @Query("SELECT * FROM coleta WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Long): ColetaEntity?

    @Insert
    suspend fun inserir(coleta: ColetaEntity): Long

    @Update
    suspend fun atualizar(coleta: ColetaEntity)

    @Delete
    suspend fun excluir(coleta: ColetaEntity)

    @Query("UPDATE coleta SET observacao = :observacao WHERE id = :coletaId")
    suspend fun atualizarObservacao(
        coletaId: Long,
        observacao: String?
    )
    @Query("UPDATE coleta SET status = :status WHERE id = :coletaId")
    suspend fun atualizarStatus(
        coletaId: Long,
        status: String
    )
    @Query("""
    SELECT * FROM coleta 
    WHERE modeloColetaId = :modeloId 
    ORDER BY dataColeta DESC
""")
    fun listarPorModelo(
        modeloId: Long
    ): Flow<List<ColetaEntity>>

    @Query("SELECT * FROM coleta WHERE id IN (:ids) ORDER BY nomeReferencia")
    suspend fun listarPorIds(
        ids: List<Long>
    ): List<ColetaEntity>
}