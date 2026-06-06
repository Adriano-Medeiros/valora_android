package br.com.agrobox.ruralcoleta.ui.exportacao

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.export.ExcelExportService
import br.com.agrobox.ruralcoleta.data.local.entity.ModeloColetaEntity
import br.com.agrobox.ruralcoleta.data.repository.BenfeitoriaRepository
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.FotoBenfeitoriaRepository
import br.com.agrobox.ruralcoleta.data.repository.FotoColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.ModeloColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.RespostaColetaRepository
import br.com.agrobox.ruralcoleta.util.ShareHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ExportacaoColetasViewModel(
    private val modeloId: Long,
    private val coletaRepository: ColetaRepository,
    private val modeloColetaRepository: ModeloColetaRepository,
    private val respostaColetaRepository: RespostaColetaRepository,
    private val fotoColetaRepository: FotoColetaRepository,
    private val benfeitoriaRepository: BenfeitoriaRepository,
    private val fotoBenfeitoriaRepository: FotoBenfeitoriaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExportacaoColetasUiState())
    val uiState = _uiState.asStateFlow()

    init {
        carregarColetas()
    }

    private fun carregarColetas() {
        viewModelScope.launch {
            coletaRepository.listarPorModelo(modeloId).collect { coletas ->
                _uiState.value = _uiState.value.copy(
                    coletas = coletas
                )
            }
        }
    }

    fun alternarSelecao(coletaId: Long) {
        val selecionadas = _uiState.value.selecionadas.toMutableSet()

        if (selecionadas.contains(coletaId)) {
            selecionadas.remove(coletaId)
        } else {
            selecionadas.add(coletaId)
        }

        _uiState.value = _uiState.value.copy(
            selecionadas = selecionadas
        )
    }

    fun selecionarTodas() {
        _uiState.value = _uiState.value.copy(
            selecionadas = _uiState.value.coletas.map { it.id }.toSet()
        )
    }

    fun limparSelecao() {
        _uiState.value = _uiState.value.copy(
            selecionadas = emptySet()
        )
    }

    fun exportarExcel(context: Context) {
        val idsSelecionados = _uiState.value.selecionadas.toList()

        if (idsSelecionados.isEmpty()) {
            return
        }

        viewModelScope.launch {
            val modelo = buscarModeloAtual()

            val coletas = coletaRepository.listarPorIds(
                idsSelecionados
            )

            val variaveis = modeloColetaRepository
                .listarVariaveisDetalhadasPorModelo(modeloId)
                .first()

            val respostasPorColeta = coletas.associate { coleta ->
                coleta.id to respostaColetaRepository
                    .listarPorColeta(coleta.id)
                    .first()
            }

            val fotoPrincipalPorColeta = coletas.associate { coleta ->
                val fotoPrincipal = fotoColetaRepository
                    .listarPorColeta(coleta.id)
                    .first()
                    .firstOrNull()

                coleta.id to fotoPrincipal?.caminhoArquivo
            }

            val fotosBenfeitoriasPorColeta = coletas.associate { coleta ->
                val benfeitorias = benfeitoriaRepository
                    .listarPorColeta(coleta.id)
                    .first()

                val fotos = benfeitorias.flatMap { benfeitoria ->
                    fotoBenfeitoriaRepository
                        .listarPorBenfeitoria(benfeitoria.id)
                        .first()
                        .map { foto -> foto.caminhoArquivo }
                }

                coleta.id to fotos
            }

            val arquivo = ExcelExportService().exportarColetas(
                context = context,
                nomeModelo = modelo?.nome ?: "coletas",
                coletas = coletas,
                variaveis = variaveis,
                respostasPorColeta = respostasPorColeta,
                fotoPrincipalPorColeta = fotoPrincipalPorColeta,
                fotosBenfeitoriasPorColeta = fotosBenfeitoriasPorColeta
            )

            ShareHelper.compartilharArquivo(
                context = context,
                file = arquivo,
                mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                titulo = "Compartilhar planilha"
            )
        }
    }

    private suspend fun buscarModeloAtual(): ModeloColetaEntity? {
        return modeloColetaRepository
            .listarAtivos()
            .first()
            .firstOrNull { it.id == modeloId }
    }
}