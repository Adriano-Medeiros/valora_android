package br.com.agrobox.ruralcoleta.ui.coleta

import br.com.agrobox.ruralcoleta.data.local.entity.TipoColeta

data class NovaColetaUiState(
    val tipoColeta: TipoColeta = TipoColeta.AVALIANDO,
    val modeloColetaId: Long? = null,

    val nomeReferencia: String = "",
    val municipio: String = "",
    val uf: String = "",

    val latitude: String = "",
    val longitude: String = "",

    val informante: String = "",
    val contatoInformante: String = "",

    val observacao: String = "",

    val erroNomeReferencia: Boolean = false,
    val erroMunicipio: Boolean = false,
    val erroUf: Boolean = false,
    val erroInformante: Boolean = false,
    val erroContatoInformante: Boolean = false,

    val mensagemErro: String? = null,

    val salvando: Boolean = false
)