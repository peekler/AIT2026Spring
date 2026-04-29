package hu.bme.aut.aitmapdemo.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.viewmodel.compose.viewModel
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
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun MyMapScreen(
    modifier: Modifier = Modifier,
    viewmodel: MyMapViewmodel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    var cameraState = rememberCameraPositionState {
        CameraPosition.fromLatLngZoom(
            LatLng(47.0, 19.0),
            10f
        )
    }

    var uiSettings by remember { mutableStateOf(
        MapUiSettings(
            zoomControlsEnabled = true,
            zoomGesturesEnabled = true,
            scrollGesturesEnabled = true,
            compassEnabled = true,
            mapToolbarEnabled = true
        )
    ) }

    val context = LocalContext.current
    var properties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.SATELLITE,
                isTrafficEnabled = true,
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                    context,
                    hu.bme.aut.aitmapdemo.R.raw.mapconfig
                )
            )
        )
    }


    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Button(
            onClick = {
                if (properties.mapType == MapType.SATELLITE) {
                    properties = MapProperties(
                        mapType = MapType.NORMAL,
                        mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                            context,
                            hu.bme.aut.aitmapdemo.R.raw.mapconfig
                        )
                    )
                } else {
                    properties = MapProperties(
                        mapType = MapType.SATELLITE
                    )
                }
            }
        ) {
            Text(text = "Switch")
        }
        var markerState by remember {
            mutableStateOf(
                MarkerState(position = LatLng(
                    47.0, 19.0))
            )
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = properties,
            uiSettings = uiSettings,
            cameraPositionState = cameraState,
            onMapLongClick = {
                //markerState = MarkerState(position = it)

                viewmodel.addMarkerPosition(it)
            },
            onMapClick ={
                val random = Random(System.currentTimeMillis())

                val cameraPosition = CameraPosition.Builder()
                    .target(it)
                    .zoom(1f+random.nextInt(10))
                    .tilt(30f+random.nextInt(15))
                    .bearing(-45f+random.nextInt(90))
                    .build()

                coroutineScope.launch {
                    cameraState.animate(
                        CameraUpdateFactory.newCameraPosition(
                            cameraPosition),
                        durationMs = 1000
                    )
                }

            }
        ) {

            Marker(
                state = markerState,
                title = "AIT Marker",
                snippet = "Marker in AIT with some details",
                draggable = true
            )

            for (markerPosition in viewmodel.getMarkersList()){
                var makerStateTmp by remember {
                    mutableStateOf(MarkerState(
                        position = markerPosition))
                }
                Marker(
                    state = makerStateTmp,
                    title = "AIT Marker",
                    snippet = "Marker in AIT with some details",
                    draggable = true
                )
            }

            Polyline(
                points = listOf(
                    LatLng(44.811058, 20.4617586),
                    LatLng(44.811058, 20.4627586),
                    LatLng(42.810058, 20.4627586),
                    LatLng(48.809058, 20.4627586),
                    LatLng(14.809058, 20.4617586),
                    LatLng(47.0, 19.0)
                ), color = androidx.compose.ui.graphics.Color.Red
            )

        }
    }
}