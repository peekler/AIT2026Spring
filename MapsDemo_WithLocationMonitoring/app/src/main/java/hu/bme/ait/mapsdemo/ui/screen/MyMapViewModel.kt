package hu.bme.ait.mapsdemo.ui.screen

import android.location.Location
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.ait.mapsdemo.location.LocationManager
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyMapViewModel @Inject constructor(
    val locationManager: LocationManager
) : ViewModel() {
    private var _markerPositionList =
        mutableStateListOf<LatLng>()

    fun getMarkersList(): List<LatLng> {
        return _markerPositionList
    }

    fun addMarkerPosition(latLng: LatLng) {
        _markerPositionList.add(latLng)
    }

    var locationState = mutableStateOf<Location?>(null)

    fun startLocationMonitoring() {
        viewModelScope.launch {
            locationManager
                .fetchUpdates()
                .collect {
                    //addMarkerPosition(LatLng(it.latitude,it.longitude))
                    locationState.value = it
                }
        }
    }

}