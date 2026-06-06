package br.com.agrobox.ruralcoleta.ui.coleta.editar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.local.entity.StatusColeta
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditarDadosGeraisColetaViewModel(
    private val coletaId: Long,
    private val coletaRepository: ColetaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditarDadosGeraisColetaUiState())
    val uiState = _uiState.asStateFlow()

    init {
        carregarColeta()
    }

    private fun carregarColeta() {
        viewModelScope.launch {
            val coleta = coletaRepository.buscarPorId(coletaId)

            if (coleta != null) {
                _uiState.value = _uiState.value.copy(
                    coletaOriginal = coleta,
                    nomeReferencia = coleta.nomeReferencia,
                    municipio = coleta.municipio,
                    uf = coleta.uf,
                    latitude = coleta.latitude?.toString().orEmpty(),
                    longitude = coleta.longitude?.toString().orEmpty(),
                    informante = coleta.informante,
                    contatoInformante = coleta.contatoInformante,
                    observacao = coleta.observacao.orEmpty()
                )
            }
        }
    }

    fun alterarNome(nome: String) {
        _uiState.value = _uiState.value.copy(
            nomeReferencia = nome,
            erroNomeReferencia = false,
            mensagemErro = null
        )
    }

    fun alterarMunicipio(municipio: String) {
        _uiState.value = _uiState.value.copy(
            municipio = municipio,
            erroMunicipio = false,
            mensagemErro = null
        )
    }

    fun alterarUf(uf: String) {
        _uiState.value = _uiState.value.copy(
            uf = uf.uppercase(),
            erroUf = false,
            mensagemErro = null
        )
    }

    fun alterarLatitude(latitude: String) {
        _uiState.value = _uiState.value.copy(latitude = latitude)
    }

    fun alterarLongitude(longitude: String) {
        _uiState.value = _uiState.value.copy(longitude = longitude)
    }

    fun alterarInformante(informante: String) {
        _uiState.value = _uiState.value.copy(
            informante = informante,
            erroInformante = false,
            mensagemErro = null
        )
    }

    fun alterarContatoInformante(contato: String) {
        _uiState.value = _uiState.value.copy(
            contatoInformante = contato,
            erroContatoInformante = false,
            mensagemErro = null
        )
    }

    fun alterarObservacao(observacao: String) {
        _uiState.value = _uiState.value.copy(observacao = observacao)
    }

    private fun validar(): Boolean {
        val state = _uiState.value

        val erroNome = state.nomeReferencia.isBlank()

        if (
            erroNome
        ) {
            _uiState.value = state.copy(
                erroNomeReferencia = erroNome,
                mensagemErro = "Preencha todos os campos obrigatórios."
            )

            return false
        }

        return true
    }

    fun salvar(
        onSuccess: (Long) -> Unit
    ) {
        if (!validar()) {
            return
        }

        val state = _uiState.value
        val coletaOriginal = state.coletaOriginal ?: return

        viewModelScope.launch {
            _uiState.value = state.copy(salvando = true)

            val coletaAtualizada = coletaOriginal.copy(
                nomeReferencia = state.nomeReferencia.trim(),
                municipio = state.municipio.trim(),
                uf = state.uf.trim(),
                latitude = state.latitude.replace(",", ".").toDoubleOrNull(),
                longitude = state.longitude.replace(",", ".").toDoubleOrNull(),
                informante = state.informante.trim(),
                contatoInformante = state.contatoInformante.trim(),
                observacao = state.observacao.trim().ifBlank { null },
                status = StatusColeta.RASCUNHO.name
            )

            coletaRepository.atualizar(coletaAtualizada)

            _uiState.value = _uiState.value.copy(salvando = false)

            onSuccess(coletaOriginal.id)
        }
    }
}