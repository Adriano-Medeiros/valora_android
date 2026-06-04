package br.com.agrobox.ruralcoleta.ui.coleta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agrobox.ruralcoleta.data.local.entity.ColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.StatusColeta
import br.com.agrobox.ruralcoleta.data.local.entity.TipoColeta
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NovaColetaViewModel(
    private val repository: ColetaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NovaColetaUiState())
    val uiState = _uiState.asStateFlow()

    fun alterarTipo(tipo: TipoColeta) {
        _uiState.value = _uiState.value.copy(
            tipoColeta = tipo
        )
    }

    fun alterarModelo(modeloColetaId: Long) {
        _uiState.value = _uiState.value.copy(
            modeloColetaId = modeloColetaId
        )
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
        _uiState.value = _uiState.value.copy(
            latitude = latitude
        )
    }

    fun alterarLongitude(longitude: String) {
        _uiState.value = _uiState.value.copy(
            longitude = longitude
        )
    }

    fun alterarObservacao(observacao: String) {
        _uiState.value = _uiState.value.copy(
            observacao = observacao
        )
    }

    private fun validarDadosGerais(): Boolean {

        val state = _uiState.value

        val erroNome = state.nomeReferencia.isBlank()

        val erroMunicipio = state.municipio.isBlank()

        val erroUf = state.uf.isBlank()

        val erroInformante = state.informante.isBlank()

        val erroContato = state.contatoInformante.isBlank()

        if (
            erroNome ||
            erroMunicipio ||
            erroUf ||
            erroInformante ||
            erroContato
        ) {

            _uiState.value = state.copy(
                erroNomeReferencia = erroNome,
                erroMunicipio = erroMunicipio,
                erroUf = erroUf,
                erroInformante = erroInformante,
                erroContatoInformante = erroContato,
                mensagemErro = "Preencha todos os campos obrigatórios."
            )

            return false
        }

        return true
    }

    fun salvar(
        onSuccess: (Long) -> Unit
    ) {
        if (!validarDadosGerais()) {
            return
        }

        viewModelScope.launch {
            val state = _uiState.value

            _uiState.value = state.copy(
                salvando = true
            )

            val coleta = ColetaEntity(
                tipoColeta = state.tipoColeta.name,

                modeloColetaId = state.modeloColetaId,

                nomeReferencia = state.nomeReferencia.trim(),

                municipio = state.municipio.trim(),

                uf = state.uf.trim(),

                latitude = state.latitude
                    .replace(",", ".")
                    .toDoubleOrNull(),

                longitude = state.longitude
                    .replace(",", ".")
                    .toDoubleOrNull(),

                informante = state.informante.trim(),

                contatoInformante = state.contatoInformante.trim(),

                status = StatusColeta.RASCUNHO.name
            )

            val coletaId = repository.salvar(coleta)

            _uiState.value = state.copy(
                salvando = false
            )

            onSuccess(coletaId)
        }
    }
    fun alterarInformante(
        informante: String
    ) {
        _uiState.value = _uiState.value.copy(
            informante = informante,
            erroInformante = false,
            mensagemErro = null
        )
    }

    fun alterarContatoInformante(
        contato: String
    ) {
        _uiState.value = _uiState.value.copy(
            contatoInformante = contato,
            erroContatoInformante = false,
            mensagemErro = null
        )
    }

}