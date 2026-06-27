package br.com.agrobox.ruralcoleta.ui.backup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BackupViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(BackupViewModel::class.java)) {
            return BackupViewModel() as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}
