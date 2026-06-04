package br.com.agrobox.ruralcoleta.data.repository

import br.com.agrobox.ruralcoleta.data.local.dao.ColetaDao
import br.com.agrobox.ruralcoleta.data.local.entity.ColetaEntity
import kotlinx.coroutines.flow.Flow
import br.com.agrobox.ruralcoleta.data.local.entity.StatusColeta

class ColetaRepository(
    private val dao: ColetaDao
) {

    fun listarTodas(): Flow<List<ColetaEntity>> {
        return dao.listarTodas()
    }

    fun listarPorTipo(tipo: String): Flow<List<ColetaEntity>> {
        return dao.listarPorTipo(tipo)
    }

    suspend fun buscarPorId(id: Long): ColetaEntity? {
        return dao.buscarPorId(id)
    }

    suspend fun salvar(coleta: ColetaEntity): Long {
        return dao.inserir(coleta)
    }

    suspend fun atualizar(coleta: ColetaEntity) {
        dao.atualizar(coleta)
    }

    suspend fun excluir(coleta: ColetaEntity) {
        dao.excluir(coleta)
    }

    suspend fun atualizarObservacao(
        coletaId: Long,
        observacao: String?
    ) {
        dao.atualizarObservacao(
            coletaId = coletaId,
            observacao = observacao
        )
    }
    suspend fun marcarComoConcluida(
        coletaId: Long
    ) {
        dao.atualizarStatus(
            coletaId = coletaId,
            status = StatusColeta.CONCLUIDA.name
        )
    }

    suspend fun marcarComoRascunho(
        coletaId: Long
    ) {
        dao.atualizarStatus(
            coletaId = coletaId,
            status = StatusColeta.RASCUNHO.name
        )
    }
    fun listarPorModelo(
        modeloId: Long
    ): Flow<List<ColetaEntity>> {
        return dao.listarPorModelo(modeloId)
    }
    suspend fun listarPorIds(
        ids: List<Long>
    ): List<ColetaEntity> {
        return dao.listarPorIds(ids)
    }

}