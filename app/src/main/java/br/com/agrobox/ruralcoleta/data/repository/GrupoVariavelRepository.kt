package br.com.agrobox.ruralcoleta.data.repository

import br.com.agrobox.ruralcoleta.data.local.dao.GrupoVariavelDao
import br.com.agrobox.ruralcoleta.data.local.entity.GrupoVariavelEntity
import kotlinx.coroutines.flow.Flow

class GrupoVariavelRepository(
    private val dao: GrupoVariavelDao
) {

    fun listarAtivos(): Flow<List<GrupoVariavelEntity>> {
        return dao.listarAtivos()
    }

    suspend fun buscarPorId(id: Long): GrupoVariavelEntity? {
        return dao.buscarPorId(id)
    }

    suspend fun salvar(grupo: GrupoVariavelEntity): Long {
        return dao.inserir(grupo)
    }

    suspend fun atualizar(grupo: GrupoVariavelEntity) {
        dao.atualizar(grupo)
    }

    suspend fun excluir(grupo: GrupoVariavelEntity) {
        dao.excluir(grupo)
    }
}
