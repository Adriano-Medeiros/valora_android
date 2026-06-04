package br.com.agrobox.ruralcoleta.data.repository

import br.com.agrobox.ruralcoleta.data.local.dao.OpcaoVariavelDao
import br.com.agrobox.ruralcoleta.data.local.entity.OpcaoVariavelEntity
import kotlinx.coroutines.flow.Flow

class OpcaoVariavelRepository(
    private val dao: OpcaoVariavelDao
) {

    fun listarPorVariavel(
        variavelId: Long
    ): Flow<List<OpcaoVariavelEntity>> {
        return dao.listarPorVariavel(variavelId)
    }

    suspend fun salvar(
        opcao: OpcaoVariavelEntity
    ): Long {
        return dao.inserir(opcao)
    }

    suspend fun excluir(
        opcao: OpcaoVariavelEntity
    ) {
        dao.excluir(opcao)
    }
}