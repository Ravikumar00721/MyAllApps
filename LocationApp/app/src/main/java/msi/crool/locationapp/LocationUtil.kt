package msi.crool.locationapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import android.location.Address
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.util.Locale


//fun hasLocationPermission(context: Context): Boolean {
//    val fineLocationPermission = ContextCompat.checkSelfPermission(
//        context,
//        Manifest.permission.ACCESS_FINE_LOCATION
//    )
//    val coarseLocationPermission = ContextCompat.checkSelfPermission(
//        context,
//        Manifest.permission.ACCESS_COARSE_LOCATION
//    )
//
//    if (fineLocationPermission == PackageManager.PERMISSION_GRANTED &&
//        coarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
//        return true
//    } else {
//        return false
//    }
//}
class LocationUtil(val context: Context) {
    private val _fusedLocationClient:FusedLocationProviderClient=
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun requestLocationObjects(viewModel: LocationViewModel)
    {
        val locationCallback=object :LocationCallback()
        {
            override fun onLocationResult(locationResult:LocationResult)
            {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    val location=LocationData(latitude=it.latitude, longitude=it.longitude)
                    viewModel.updateLocation(location)
                }
            }
        }
        val locationRequest= LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,1000
        ).build()

        _fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,Looper.getMainLooper())
    }


    fun hasLocationPermission(context:Context):Boolean
    {
          return ContextCompat.checkSelfPermission(
              context,
              Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED //0 means yes here
                  &&
              ContextCompat.checkSelfPermission(
                  context,
                  Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED

    }

    fun reverseGeocodelocation(location:LocationData):String{
        val geocoder=Geocoder(context, Locale.getDefault())
        val coordinate= LatLng(location.latitude,location.longitude)
        val address:MutableList<Address>?=
            geocoder.getFromLocation(coordinate.latitude,coordinate.longitude,1)
        return if(address?.isNotEmpty()==true)
        {
            address[0].getAddressLine(0)
        }
        else{
            "Address Not Found"
        }

    }
}