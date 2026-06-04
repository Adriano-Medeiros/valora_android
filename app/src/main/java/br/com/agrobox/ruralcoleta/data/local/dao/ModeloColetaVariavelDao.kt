package br.com.agrobox.ruralcoleta.data.local.dao

import androidx.room.*
import br.com.agrobox.ruralcoleta.data.local.entity.ModeloColetaVariavelEntity
import br.com.agrobox.ruralcoleta.data.local.entity.VariavelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ModeloColetaVariavelDao {

    @Query("""
    SELECT * FROM modelo_coleta_variavel 
    WHERE modeloColetaId = :modeloColetaId 
    ORDER BY ordem
  """)
    fun listarPorModelo(modeloColetaId: Long): Flow<List<ModeloColetaVariavelEntity>>

    @Insert
    suspend fun inserir(item: ModeloColetaVariavelEntity): Long

    @Insert
    suspend fun inserirTodos(itens: List<ModeloColetaVariavelEntity>)

    @Update
    suspend fun atualizar(item: ModeloColetaVariavelEntity)

    @Delete
    suspend fun excluir(item: ModeloColetaVariavelEntity)

    @Query("DELETE FROM modelo_coleta_variavel WHERE modeloColetaId = :modeloColetaId")
    suspend fun excluirPorModelo(modeloColetaId: Long)

    @Query("""
    SELECT v.* FROM variavel v
    INNER JOIN modelo_coleta_variavel mcv 
        ON v.id = mcv.variavelId
    WHERE mcv.modeloColetaId = :modeloColetaId
    ORDER BY mcv.ordem
""")
    fun listarVariaveisDetalhadasPorModelo(
        modeloColetaId: Long
    ): Flow<List<VariavelEntity>>
}