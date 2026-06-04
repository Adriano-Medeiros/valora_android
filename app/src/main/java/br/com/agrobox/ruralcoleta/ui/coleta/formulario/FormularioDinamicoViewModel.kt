package br.com.agrobox.ruralcoleta.ui.coleta.formulario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.local.entity.RespostaColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.TipoCampo
import br.com.agrobox.ruralcoleta.data.local.entity.VariavelEntity
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.ModeloColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.OpcaoVariavelRepository
import br.com.agrobox.ruralcoleta.data.repository.RespostaColetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FormularioDinamicoViewModel(
    private val coletaId: Long,
    private val coletaRepository: ColetaRepository,
    private val modeloRepository: ModeloColetaRepository,
    private val respostaRepository: RespostaColetaRepository,
    private val opcaoVariavelRepository: OpcaoVariavelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        FormularioDinamicoUiState(
            coletaId = coletaId
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        carregarFormulario()
        carregarRespostasSalvas()
    }

    private fun carregarFormulario() {
        viewModelScope.launch {
            val coleta = coletaRepository.buscarPorId(coletaId)
            val modeloId = coleta?.modeloColetaId

            _uiState.value = _uiState.value.copy(
                modeloColetaId = modeloId
            )

            if (modeloId != null) {
                modeloRepository
                    .listarVariaveisDetalhadasPorModelo(modeloId)
                    .collect { variaveis ->

                        _uiState.value = _uiState.value.copy(
                            variaveis = variaveis
                        )

                        carregarOpcoesDasListas(variaveis)
                    }
            }
        }
    }

    private fun carregarRespostasSalvas() {
        viewModelScope.launch {
            respostaRepository.listarPorColeta(coletaId).collect { respostas ->
                val respostasMap = respostas.associate { resposta ->
                    resposta.variavelId to converterRespostaParaTexto(resposta)
                }

                _uiState.value = _uiState.value.copy(
                    respostas = respostasMap
                )
            }
        }
    }

    private fun converterRespostaParaTexto(
        resposta: RespostaColetaEntity
    ): String {
        return when {
            resposta.opcaoId != null -> resposta.opcaoId.toString()
            resposta.valorTexto != null -> resposta.valorTexto
            resposta.valorNumero != null -> resposta.valorNumero.toString()
            resposta.valorBooleano != null -> {
                if (resposta.valorBooleano) "SIM" else "NAO"
            }
            resposta.valorData != null -> resposta.valorData.toString()
            else -> ""
        }
    }

    private fun carregarOpcoesDasListas(
        variaveis: List<VariavelEntity>
    ) {
        variaveis
            .filter { it.tipoCampo == TipoCampo.LISTA.name }
            .forEach { variavel ->

                viewModelScope.launch {
                    opcaoVariavelRepository
                        .listarPorVariavel(variavel.id)
                        .collect { opcoes ->

                            val mapaAtual = _uiState.value.opcoesPorVariavel.toMutableMap()

                            mapaAtual[variavel.id] = opcoes

                            _uiState.value = _uiState.value.copy(
                                opcoesPorVariavel = mapaAtual
                            )
                        }
                }
            }
    }

    fun alterarResposta(
        variavelId: Long,
        valor: String
    ) {
        val camposComErroAtualizados = _uiState.value.camposComErro
            .filter { it != variavelId }
            .toSet()

        _uiState.value = _uiState.value.copy(
            respostas = _uiState.value.respostas + (variavelId to valor),
            camposComErro = camposComErroAtualizados,
            mensagemErro = null
        )
    }

    fun salvarRespostas(
        onSuccess: () -> Unit
    ) {
        val state = _uiState.value

        val camposObrigatoriosNaoPreenchidos = state.variaveis
            .filter { variavel ->
                variavel.obrigatoria &&
                        state.respostas[variavel.id].isNullOrBlank()
            }
            .map { it.id }
            .toSet()

        if (camposObrigatoriosNaoPreenchidos.isNotEmpty()) {
            _uiState.value = state.copy(
                camposComErro = camposObrigatoriosNaoPreenchidos,
                mensagemErro = "Preencha todos os campos obrigatórios."
            )

            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(
                salvando = true,
                mensagemErro = null
            )

            respostaRepository.excluirPorColeta(coletaId)

            val respostas = state.variaveis.mapNotNull { variavel ->
                val valor = state.respostas[variavel.id] ?: return@mapNotNull null

                when (variavel.tipoCampo) {
                    TipoCampo.NUMERO.name,
                    TipoCampo.MOEDA.name,
                    TipoCampo.PERCENTUAL.name -> {
                        RespostaColetaEntity(
                            coletaId = coletaId,
                            variavelId = variavel.id,
                            valorNumero = valor.replace(",", ".").toDoubleOrNull()
                        )
                    }

                    TipoCampo.SIM_NAO.name -> {
                        RespostaColetaEntity(
                            coletaId = coletaId,
                            variavelId = variavel.id,
                            valorBooleano = valor == "SIM"
                        )
                    }

                    TipoCampo.LISTA.name -> {
                        val opcaoId = valor.toLongOrNull()

                        RespostaColetaEntity(
                            coletaId = coletaId,
                            variavelId = variavel.id,
                            opcaoId = opcaoId,
                            valorTexto = state.opcoesPorVariavel[variavel.id]
                                ?.firstOrNull { it.id == opcaoId }
                                ?.descricao
                        )
                    }

                    else -> {
                        RespostaColetaEntity(
                            coletaId = coletaId,
                            variavelId = variavel.id,
                            valorTexto = valor
                        )
                    }
                }
            }

            respostaRepository.salvarTodas(respostas)

            _uiState.value = _uiState.value.copy(
                salvando = false
            )

            onSuccess()
        }
    }
}