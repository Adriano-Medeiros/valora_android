package br.com.agrobox.ruralcoleta.data.repository

import br.com.agrobox.ruralcoleta.data.local.dao.RespostaColetaDao
import br.com.agrobox.ruralcoleta.data.local.entity.RespostaColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.RespostaDetalhada
import kotlinx.coroutines.flow.Flow

class RespostaColetaRepository(
    private val dao: RespostaColetaDao
) {

    fun listarPorColeta(
        coletaId: Long
    ): Flow<List<RespostaColetaEntity>> {
        return dao.listarPorColeta(coletaId)
    }

    suspend fun salvarTodas(
        respostas: List<RespostaColetaEntity>
    ) {
        dao.inserirTodas(respostas)
    }

    suspend fun excluirPorColeta(
        coletaId: Long
    ) {
        dao.excluirPorColeta(coletaId)
    }
    fun listarDetalhadasPorColeta(
        coletaId: Long
    ): Flow<List<RespostaDetalhada>> {
        return dao.listarDetalhadasPorColeta(coletaId)
    }
}