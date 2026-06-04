package br.com.agrobox.ruralcoleta.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.PreferenciasRepository

class DashboardViewModelFactory(
    private val coletaRepository: ColetaRepository,
    private val preferenciasRepository: PreferenciasRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(
                coletaRepository = coletaRepository,
                preferenciasRepository = preferenciasRepository
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}