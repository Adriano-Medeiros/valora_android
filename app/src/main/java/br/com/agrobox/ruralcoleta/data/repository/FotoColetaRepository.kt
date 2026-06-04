package br.com.agrobox.ruralcoleta.data.repository

import br.com.agrobox.ruralcoleta.data.local.dao.FotoColetaDao
import br.com.agrobox.ruralcoleta.data.local.entity.FotoColetaEntity
import kotlinx.coroutines.flow.Flow

class FotoColetaRepository(
    private val dao: FotoColetaDao
) {

    fun listarPorColeta(
        coletaId: Long
    ): Flow<List<FotoColetaEntity>> {
        return dao.listarPorColeta(coletaId)
    }

    suspend fun salvar(
        foto: FotoColetaEntity
    ): Long {
        return dao.inserir(foto)
    }

    suspend fun atualizar(
        foto: FotoColetaEntity
    ) {
        dao.atualizar(foto)
    }

    suspend fun excluir(
        foto: FotoColetaEntity
    ) {
        dao.excluir(foto)
    }
}