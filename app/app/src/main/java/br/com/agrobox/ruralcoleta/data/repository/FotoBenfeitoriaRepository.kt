package br.com.agrobox.ruralcoleta.data.repository

import br.com.agrobox.ruralcoleta.data.local.dao.FotoBenfeitoriaDao
import br.com.agrobox.ruralcoleta.data.local.entity.FotoBenfeitoriaEntity
import kotlinx.coroutines.flow.Flow

class FotoBenfeitoriaRepository(
    private val dao: FotoBenfeitoriaDao
) {

    fun listarPorBenfeitoria(
        benfeitoriaId: Long
    ): Flow<List<FotoBenfeitoriaEntity>> {
        return dao.listarPorBenfeitoria(benfeitoriaId)
    }

    suspend fun salvar(
        foto: FotoBenfeitoriaEntity
    ): Long {
        return dao.inserir(foto)
    }

    suspend fun atualizar(
        foto: FotoBenfeitoriaEntity
    ) {
        dao.atualizar(foto)
    }

    suspend fun excluir(
        foto: FotoBenfeitoriaEntity
    ) {
        dao.excluir(foto)
    }
}
