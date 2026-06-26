package br.com.agrobox.ruralcoleta.data.repository

import br.com.agrobox.ruralcoleta.data.local.dao.ModeloColetaDao
import br.com.agrobox.ruralcoleta.data.local.dao.ModeloColetaVariavelDao
import br.com.agrobox.ruralcoleta.data.local.entity.ModeloColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.ModeloColetaVariavelEntity
import br.com.agrobox.ruralcoleta.data.local.entity.VariavelEntity
import kotlinx.coroutines.flow.Flow

class ModeloColetaRepository(
    private val modeloDao: ModeloColetaDao,
    private val modeloVariavelDao: ModeloColetaVariavelDao
) {

    fun listarAtivos(): Flow<List<ModeloColetaEntity>> {
        return modeloDao.listarAtivos()
    }

    suspend fun buscarModeloPorId(id: Long): ModeloColetaEntity? {
        return modeloDao.buscarPorId(id)
    }

    fun listarVariaveisDoModelo(
        modeloColetaId: Long
    ): Flow<List<ModeloColetaVariavelEntity>> {
        return modeloVariavelDao.listarPorModelo(modeloColetaId)
    }

    suspend fun listarIdsVariaveisDoModelo(
        modeloColetaId: Long
    ): List<Long> {
        return modeloVariavelDao.listarIdsPorModelo(modeloColetaId)
    }

    suspend fun salvarModelo(
        modelo: ModeloColetaEntity
    ): Long {
        return modeloDao.inserir(modelo)
    }

    suspend fun atualizarModelo(
        modelo: ModeloColetaEntity
    ) {
        modeloDao.atualizar(modelo)
    }

    suspend fun excluirModelo(
        modelo: ModeloColetaEntity
    ) {
        modeloDao.excluir(modelo)
    }

    suspend fun salvarVariaveisDoModelo(
        modeloColetaId: Long,
        variaveisIds: List<Long>
    ) {
        modeloVariavelDao.excluirPorModelo(modeloColetaId)

        val itens = variaveisIds.mapIndexed { index, variavelId ->
            ModeloColetaVariavelEntity(
                modeloColetaId = modeloColetaId,
                variavelId = variavelId,
                ordem = index + 1,
                obrigatoriaNoModelo = false
            )
        }

        if (itens.isNotEmpty()) {
            modeloVariavelDao.inserirTodos(itens)
        }
    }

    fun listarVariaveisDetalhadasPorModelo(
        modeloColetaId: Long
    ): Flow<List<VariavelEntity>> {
        return modeloVariavelDao.listarVariaveisDetalhadasPorModelo(modeloColetaId)
    }
}
