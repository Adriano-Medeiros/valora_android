package br.com.agrobox.ruralcoleta.ui.coleta.resumo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.repository.BenfeitoriaRepository
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.FotoColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.ModeloColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.RespostaColetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ResumoColetaViewModel(
    private val coletaId: Long,
    private val coletaRepository: ColetaRepository,
    private val respostaRepository: RespostaColetaRepository,
    private val benfeitoriaRepository: BenfeitoriaRepository,
    private val fotoColetaRepository: FotoColetaRepository,
    private val modeloColetaRepository: ModeloColetaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResumoColetaUiState())
    val uiState = _uiState.asStateFlow()

    init {
        carregarResumo()
    }

    private fun carregarResumo() {
        viewModelScope.launch {
            val coleta = coletaRepository.buscarPorId(coletaId)

            _uiState.value = _uiState.value.copy(
                coleta = coleta
            )
        }

        viewModelScope.launch {
            respostaRepository.listarPorColeta(coletaId).collect { respostas ->
                _uiState.value = _uiState.value.copy(
                    respostas = respostas
                )
            }
        }

        viewModelScope.launch {
            benfeitoriaRepository.listarPorColeta(coletaId).collect { benfeitorias ->
                _uiState.value = _uiState.value.copy(
                    benfeitorias = benfeitorias
                )
            }
        }

        viewModelScope.launch {
            fotoColetaRepository.listarPorColeta(coletaId).collect { fotos ->
                _uiState.value = _uiState.value.copy(
                    fotos = fotos
                )
            }
        }
    }

    fun finalizarColeta(
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            val pendencias = validarPendencias()

            if (pendencias.isNotEmpty()) {

                coletaRepository.marcarComoRascunho(
                    coletaId
                )

                _uiState.value = _uiState.value.copy(
                    pendencias = pendencias,
                    mensagemErro = "A coleta continua como rascunho. Existem campos obrigatórios pendentes."
                )

                return@launch
            }

            coletaRepository.marcarComoConcluida(
                coletaId
            )

            _uiState.value = _uiState.value.copy(
                pendencias = emptyList(),
                mensagemErro = null
            )

            onSuccess()
        }
    }

    private suspend fun validarPendencias(): List<String> {
        val pendencias = mutableListOf<String>()

        val coleta = coletaRepository.buscarPorId(coletaId)

        if (coleta == null) {
            pendencias.add("Coleta não encontrada.")
            return pendencias
        }

        if (coleta.nomeReferencia.isBlank()) {
            pendencias.add("Nome / referência não informado.")
        }

        if (coleta.municipio.isBlank()) {
            pendencias.add("Município não informado.")
        }

        if (coleta.uf.isBlank()) {
            pendencias.add("UF não informada.")
        }
        if (coleta.informante.isBlank()) {
            pendencias.add("Informante não informado.")
        }

        if (coleta.contatoInformante.isBlank()) {
            pendencias.add("Contato do informante não informado.")
        }
        val modeloId = coleta.modeloColetaId

        if (modeloId == null) {
            pendencias.add("Modelo de coleta não selecionado.")
            return pendencias
        }

        val variaveisObrigatorias = modeloColetaRepository
            .listarVariaveisDetalhadasPorModelo(modeloId)
            .first()
            .filter { it.obrigatoria }

        val respostas = respostaRepository
            .listarPorColeta(coletaId)
            .first()

        variaveisObrigatorias.forEach { variavel ->
            val resposta = respostas.firstOrNull {
                it.variavelId == variavel.id
            }

            val preenchida = resposta != null &&
                    (
                            !resposta.valorTexto.isNullOrBlank() ||
                                    resposta.valorNumero != null ||
                                    resposta.valorBooleano != null ||
                                    resposta.valorData != null ||
                                    resposta.opcaoId != null
                            )

            if (!preenchida) {
                pendencias.add("Campo obrigatório não preenchido: ${variavel.nome}")
            }
        }

        return pendencias
    }
}