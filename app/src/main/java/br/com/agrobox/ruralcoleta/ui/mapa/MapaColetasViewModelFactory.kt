package br.com.agrobox.ruralcoleta.ui.mapa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository

class MapaColetasViewModelFactory(
    private val coletaRepository: ColetaRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(MapaColetasViewModel::class.java)) {
            return MapaColetasViewModel(
                coletaRepository = coletaRepository
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}