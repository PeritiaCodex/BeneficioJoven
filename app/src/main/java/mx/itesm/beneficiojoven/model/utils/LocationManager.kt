package mx.itesm.beneficiojoven.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Gestor de ubicación que encapsula la lógica de obtención de coordenadas.
 * YA NO MANEJA PERMISOS, solo obtiene la ubicación cuando se le pide.
 *
 * @param context El Context de la aplicación.
 */
class LocationManager(private val context: Context) {

    /** Cliente de servicios de ubicación de Google Play. */
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    /** [MutableStateFlow] interno para la última ubicación conocida. */
    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    /** [StateFlow] público e inmutable de la ubicación actual ([LatLng] o `null`). */
    val currentLocation: StateFlow<LatLng?> = _currentLocation

    /**
     * Comprueba si el permiso está otorgado.
     */
    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Obtiene la última ubicación registrada por el dispositivo.
     * Es rápido y no consume mucha batería.
     * Solo debe llamarse si se ha verificado el permiso.
     */
    @SuppressLint("MissingPermission")
    fun getLastKnownLocation() {
        // Se asume que el permiso ya fue verificado antes de llamar a esta función.
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    _currentLocation.value = LatLng(location.latitude, location.longitude)
                } else {
                    Toast.makeText(context, "No se pudo obtener la ubicación. Activa el GPS.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error al obtener la ubicación.", Toast.LENGTH_SHORT).show()
            }
    }
}