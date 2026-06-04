package br.com.agrobox.ruralcoleta.data.repository

import br.com.agrobox.ruralcoleta.data.local.dao.BenfeitoriaDao
import br.com.agrobox.ruralcoleta.data.local.entity.BenfeitoriaComFotos
import br.com.agrobox.ruralcoleta.data.local.entity.BenfeitoriaEntity
import kotlinx.coroutines.flow.Flow

class BenfeitoriaRepository(
    private val dao: BenfeitoriaDao
) {

    fun listarPorColeta(
        coletaId: Long
    ): Flow<List<BenfeitoriaEntity>> {

        return dao.listarPorColeta(coletaId)
    }

    suspend fun salvar(
        benfeitoria: BenfeitoriaEntity
    ): Long {

        return dao.inserir(benfeitoria)
    }

    suspend fun atualizar(
        benfeitoria: BenfeitoriaEntity
    ) {

        dao.atualizar(benfeitoria)
    }

    suspend fun excluir(
        benfeitoria: BenfeitoriaEntity
    ) {

        dao.excluir(benfeitoria)
    }
    fun listarComTotalFotosPorColeta(
        coletaId: Long
    ): Flow<List<BenfeitoriaComFotos>> {
        return dao.listarComTotalFotosPorColeta(coletaId)
    }
}