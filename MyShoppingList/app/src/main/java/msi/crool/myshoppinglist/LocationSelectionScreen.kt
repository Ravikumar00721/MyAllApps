package msi.crool.myshoppinglist

import android.location.Location
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationSelectionScreen(
    location: LocationData,
    onLocationSelected: (LocationData) -> Unit
) {
    // Correct the typo 'logitude' to 'longitude'
    val userLocation = remember { mutableStateOf(LatLng(location.latitude, location.longitude)) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation.value, 10f)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier
                .weight(1f)
                .padding(top = 16.dp),
            cameraPositionState = cameraPositionState,
            onMapClick = {
                userLocation.value = it
                cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 10f) // Update camera position on map click
            }
        ) {
            Marker(state = MarkerState(position = userLocation.value))
        }
        Button(
            onClick = {
                val newLocation = LocationData(userLocation.value.latitude, userLocation.value.longitude)
                onLocationSelected(newLocation)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Set Location")
        }
    }
}
