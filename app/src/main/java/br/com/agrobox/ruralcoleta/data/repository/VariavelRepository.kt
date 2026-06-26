package br.com.agrobox.ruralcoleta.data.repository

import br.com.agrobox.ruralcoleta.data.local.dao.VariavelDao
import br.com.agrobox.ruralcoleta.data.local.entity.VariavelEntity
import kotlinx.coroutines.flow.Flow

class VariavelRepository(
    private val dao: VariavelDao
) {

    fun listarAtivas(): Flow<List<VariavelEntity>> {
        return dao.listarAtivas()
    }

    fun listarPorGrupo(grupoId: Long): Flow<List<VariavelEntity>> {
        return dao.listarPorGrupo(grupoId)
    }

    suspend fun buscarPorId(id: Long): VariavelEntity? {
        return dao.buscarPorId(id)
    }

    suspend fun salvar(variavel: VariavelEntity): Long {
        return dao.inserir(variavel)
    }

    suspend fun atualizar(variavel: VariavelEntity) {
        dao.atualizar(variavel)
    }

    suspend fun excluir(variavel: VariavelEntity) {
        dao.excluir(variavel)
    }
}
