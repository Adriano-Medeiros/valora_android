package br.com.agrobox.ruralcoleta.data.local.dao

import androidx.room.*
import br.com.agrobox.ruralcoleta.data.local.entity.RespostaColetaEntity
import kotlinx.coroutines.flow.Flow
import br.com.agrobox.ruralcoleta.data.local.entity.RespostaDetalhada
@Dao
interface RespostaColetaDao {

    @Query("SELECT * FROM resposta_coleta WHERE coletaId = :coletaId")
    fun listarPorColeta(coletaId: Long): Flow<List<RespostaColetaEntity>>

    @Insert
    suspend fun inserir(resposta: RespostaColetaEntity): Long

    @Insert
    suspend fun inserirTodas(respostas: List<RespostaColetaEntity>)

    @Update
    suspend fun atualizar(resposta: RespostaColetaEntity)

    @Query("DELETE FROM resposta_coleta WHERE coletaId = :coletaId")
    suspend fun excluirPorColeta(coletaId: Long)
    @Query("""
    SELECT 
        r.id AS respostaId,
        r.coletaId AS coletaId,
        r.variavelId AS variavelId,
        v.nome AS nomeVariavel,
        v.tipoCampo AS tipoCampo,
        v.unidade AS unidade,
        r.valorTexto AS valorTexto,
        r.valorNumero AS valorNumero,
        r.valorBooleano AS valorBooleano,
        r.valorData AS valorData,
        r.opcaoId AS opcaoId
    FROM resposta_coleta r
    INNER JOIN variavel v ON v.id = r.variavelId
    WHERE r.coletaId = :coletaId
    ORDER BY v.nome
""")
    fun listarDetalhadasPorColeta(
        coletaId: Long
    ): Flow<List<RespostaDetalhada>>
}