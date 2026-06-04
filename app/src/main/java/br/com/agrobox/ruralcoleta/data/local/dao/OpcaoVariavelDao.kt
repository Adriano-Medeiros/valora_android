package br.com.agrobox.ruralcoleta.data.local.dao

import androidx.room.*
import br.com.agrobox.ruralcoleta.data.local.entity.OpcaoVariavelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OpcaoVariavelDao {

    @Query("SELECT * FROM opcao_variavel WHERE variavelId = :variavelId AND ativo = 1 ORDER BY ordem, descricao")
    fun listarPorVariavel(variavelId: Long): Flow<List<OpcaoVariavelEntity>>

    @Insert
    suspend fun inserir(opcao: OpcaoVariavelEntity): Long

    @Update
    suspend fun atualizar(opcao: OpcaoVariavelEntity)

    @Delete
    suspend fun excluir(opcao: OpcaoVariavelEntity)
}