package br.com.agrobox.ruralcoleta.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await
import java.util.Locale

object LocationHelper {

    fun temPermissaoLocalizacao(context: Context): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fine || coarse
    }

    suspend fun capturarLocalizacaoAtual(
        context: Context
    ): Pair<String, String>? {
        if (!temPermissaoLocalizacao(context)) {
            return null
        }

        val fusedClient = LocationServices.getFusedLocationProviderClient(context)

        val currentLocation: Location? = try {
            fusedClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).await()
        } catch (e: SecurityException) {
            null
        } catch (e: Exception) {
            null
        }

        val finalLocation = currentLocation ?: try {
            fusedClient.lastLocation.await()
        } catch (e: SecurityException) {
            null
        } catch (e: Exception) {
            null
        }

        return finalLocation?.let { location ->
            Pair(
                String.format(Locale.US, "%.6f", location.latitude),
                String.format(Locale.US, "%.6f", location.longitude)
            )
        }
    }
}