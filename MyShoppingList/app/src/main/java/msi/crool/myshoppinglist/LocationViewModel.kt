package msi.crool.myshoppinglist

import android.location.Location
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LocationViewModel : ViewModel() {
    private val _location = mutableStateOf<LocationData?>(null)
    val location: State<LocationData?> = _location

    private val _address = mutableStateOf(listOf<GeoCodingResult>())
    val address: State<List<GeoCodingResult>> = _address

    fun updateLocation(newLocation: LocationData) {
        _location.value = newLocation
    }

    fun fetchAddress(latLng: String) {
        viewModelScope.launch {
            try {
                val result = RetrofitClient.create().getAddressfromCordinates(
                    latLng,
                    "AIzaSyC4nEM2_HITnLBA-aOzNQl5IPiTgPKia74" // Ensure this key is valid
                )
                _address.value = result.results

                // Log the results
                Log.d("LocationViewModel", "Address fetch successful")
                Log.d("LocationViewModel", "Result: ${result.results}")

                // Print each address result (if applicable)
                result.results.forEach { geoCodingResult ->
                    Log.d("LocationViewModel", "Formatted Address: ${geoCodingResult.Formattedadddress}")
                }

            } catch (e: HttpException) {
                // Log the detailed error response
                Log.d("LocationViewModel", "Error fetching address", e)
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("LocationViewModel", "Error body: $errorBody")
            } catch (e: Exception) {
                Log.d("LocationViewModel", "Error fetching address", e)
            }
        }
    }
}
