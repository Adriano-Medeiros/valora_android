package br.com.agrobox.ruralcoleta.data.local.dao

import androidx.room.*
import br.com.agrobox.ruralcoleta.data.local.entity.BenfeitoriaComFotos
import br.com.agrobox.ruralcoleta.data.local.entity.BenfeitoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BenfeitoriaDao {

    @Query("SELECT * FROM benfeitoria WHERE coletaId = :coletaId ORDER BY categoria, nome")
    fun listarPorColeta(coletaId: Long): Flow<List<BenfeitoriaEntity>>

    @Insert
    suspend fun inserir(benfeitoria: BenfeitoriaEntity): Long

    @Update
    suspend fun atualizar(benfeitoria: BenfeitoriaEntity)

    @Delete
    suspend fun excluir(benfeitoria: BenfeitoriaEntity)
    @Query("""
    SELECT 
        b.id AS id,
        b.coletaId AS coletaId,
        b.categoria AS categoria,
        b.nome AS nome,
        b.descricao AS descricao,
        b.quantidade AS quantidade,
        b.unidade AS unidade,
        b.estadoConservacao AS estadoConservacao,
        b.idadeAproximada AS idadeAproximada,
        b.observacao AS observacao,
        COUNT(f.id) AS totalFotos
    FROM benfeitoria b
    LEFT JOIN foto_benfeitoria f 
        ON f.benfeitoriaId = b.id
    WHERE b.coletaId = :coletaId
    GROUP BY b.id
    ORDER BY b.categoria, b.nome
""")
    fun listarComTotalFotosPorColeta(
        coletaId: Long
    ): Flow<List<BenfeitoriaComFotos>>
}