package msi.crool.weatherapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object Constants {
    const val APP_ID:String= "7c8528b4851c1e050e5aba750024ebcd"
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val METRIC_UNIT:String="metric"
    const val PrefrenceName="WeatherAppPrefrence"
    const val Weather_Response_Data="weather_response_data"

    fun isNetworkAvailable(context: Context):Boolean{
        val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as
                ConnectivityManager
        val network=connectivityManager.activeNetwork?: return false
        val activityNetwork=connectivityManager.getNetworkCapabilities(network)?:return false

        return when{
            activityNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->return true
            activityNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)->return true
            activityNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->return true
            else->false
        }
    }
}