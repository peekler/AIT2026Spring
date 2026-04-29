package hu.bme.ait.mapsdemo.ui.screen

import android.Manifest
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.MarkerState.Companion.invoke
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.ktx.model.cameraPosition
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.collections.emptyList
import kotlin.random.Random

fun getLocationText(location: Location?): String {
    return """
       Lat: ${location?.latitude}
       Lng: ${location?.longitude}
       Alt: ${location?.altitude}
       Speed: ${location?.speed}
       Accuracy: ${location?.accuracy}
    """.trimIndent()
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MyMapScreen(
    modifier: Modifier = Modifier,
    viewModel: MyMapViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var cameraState = rememberCameraPositionState {
        CameraPosition.fromLatLngZoom(
            LatLng(47.0, 19.0), 10f
        )
    }
    var uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = true,
                zoomGesturesEnabled = true
            )
        )
    }
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.SATELLITE,
                isTrafficEnabled = true,
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                    context,
                    hu.bme.ait.mapsdemo.R.raw.mapstyle
                )
            )
        )
    }



    Column(
        modifier = modifier
    ) {

        val fineLocationPermissionState = rememberPermissionState(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (fineLocationPermissionState.status.isGranted) {
            Column {

                Button(onClick = {
                    viewModel.startLocationMonitoring()
                }) {
                    Text(text = "Start location monitoring")
                }
                Text(
                    text = "Location: " +
                            "${getLocationText(viewModel.locationState.value)}"
                )
            }
        } else {
            Column() {
                val permissionText = if (fineLocationPermissionState.status.shouldShowRationale) {
                    "Please consider giving permission"
                } else {
                    "Give permission for location"
                }
                Text(text = permissionText)
                Button(onClick = {
                    fineLocationPermissionState.launchPermissionRequest()
                }) {
                    Text(text = "Request permission")
                }
            }
        }


        var isSatellite by remember { mutableStateOf(true) }
        Switch(
            checked = isSatellite,
            onCheckedChange = {
                isSatellite = it
                mapProperties = mapProperties.copy(
                    mapType = if (it) MapType.SATELLITE else MapType.NORMAL
                )
            }
        )

        var geoText by remember { mutableStateOf("") }
        Text("Addres: $geoText")


        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            cameraPositionState = cameraState,
            uiSettings = uiSettings,
            properties = mapProperties,
            onMapClick = { coordinate ->
                viewModel.addMarkerPosition(coordinate)
            },
            onMapLongClick = {
                val random = Random(System.currentTimeMillis())
                val cameraPostion = CameraPosition.Builder()
                    .target(it)
                    .zoom(1f + random.nextInt(5))
                    .tilt(30f + random.nextInt(15))
                    .bearing(-45f + random.nextInt(90))
                    .build()
                //cameraState.position = cameraPostion
                coroutineScope.launch {
                    cameraState.animate(
                        CameraUpdateFactory.newCameraPosition(cameraPostion), 3000
                    )
                }
            }
        ) {
            var markerState by remember {
                mutableStateOf(
                    MarkerState(position = LatLng(47.0, 19.0))
                )
            }

            Marker(
                state = markerState,
                title = "Marker AIT",
                snippet = "Marker long text, lorem ipsum...",
                draggable = true,
                alpha = 0.5f,
            )

            for (markerPosition in viewModel.getMarkersList()) {
                var markerStateTmp by remember {
                    mutableStateOf(
                        MarkerState(position = markerPosition)
                    )
                }

                Marker(
                    state = markerStateTmp,
                    title = "Marker",
                    snippet = "Coord: " +
                            "${markerPosition.latitude}," +
                            "${markerPosition.longitude}",
                    onClick = {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            geocoder.getFromLocation(
                                it.position.latitude,
                                it.position.longitude,
                                3,
                                object : Geocoder.GeocodeListener {
                                    override fun onGeocode(addrs: MutableList<Address>) {
                                        val addr =
                                            "${addrs[0].getAddressLine(0)}, ${
                                                addrs[0].getAddressLine(1)
                                            }, ${addrs[0].getAddressLine(2)}"
                                        geoText = addr
                                    }

                                    override fun onError(errorMessage: String?) {
                                        geoText = errorMessage!!
                                        super.onError(errorMessage)
                                    }
                                })
                        }
                        true

                    }
                )
            }

            Polyline(
                points = listOf(
                    LatLng(44.811058, 20.4617586),
                    LatLng(44.811058, 20.4627586),
                    LatLng(44.810058, 20.4627586),
                    LatLng(44.809058, 20.4627586),
                    LatLng(44.809058, 20.4617586),
                    LatLng(47.0, 19.0)
                ), color = androidx.compose.ui.graphics.Color.Green
            )


        }
    }
}