package msi.crool.weatherapp

import android.content.Context
import android.content.Intent
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.SharedPreferences
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import msi.crool.weatherapp.Models.WeatherResponse
import msi.crool.weatherapp.Network.WeatherService
import msi.crool.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class MainActivity : ComponentActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var mfusedLocation: FusedLocationProviderClient
    private var mProgressDialog: Dialog? = null
    private lateinit var mSharedPrefrences:SharedPreferences

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        mfusedLocation = LocationServices.getFusedLocationProviderClient(this)
        mSharedPrefrences=getSharedPreferences(Constants.PrefrenceName,Context.MODE_PRIVATE)

        if (!isLocationEnabled()) {
            Toast.makeText(this, "Your Location provider is Turned Off", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } else {
            Dexter.withContext(this).withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                @RequiresApi(Build.VERSION_CODES.S)
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (it.areAllPermissionsGranted()) {
                            requestLocationData()
                        }
                        if (it.isAnyPermissionPermanentlyDenied) {
                            Toast.makeText(this@MainActivity, "You have Denied Permission Location", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.let { showRationalDialogPermission() }
                }
            }).check()
        }

        binding?.refresh?.setOnClickListener {
            Toast.makeText(this@MainActivity, "Clicked", Toast.LENGTH_LONG).show()
            requestLocationData()
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    private fun requestLocationData() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).build()

        mfusedLocation.requestLocationUpdates(
            locationRequest, mLocationCallback, Looper.getMainLooper()
        )
    }

    private fun getLocationWeatherDetails(latitude: Double, longitude: Double) {
        if (Constants.isNetworkAvailable(this)) {
            Log.d("Weather Details ", "INSIDE 1")
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
            val service: WeatherService = retrofit.create(WeatherService::class.java)
            val listCall: Call<WeatherResponse> = service.getWeather(
                latitude, longitude, Constants.METRIC_UNIT, Constants.APP_ID
            )
            showDialog()
            listCall.enqueue(object : Callback<WeatherResponse> {
                @SuppressLint("CommitPrefEdits")
                override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                    Log.d("Weather Details ", "INSIDE 2")
                    if (response.isSuccessful) {
                        hideDialog()
                        val weatherList: WeatherResponse? = response.body()
                        val weatherJsonResponseString= Gson().toJson(weatherList)
                        val editor=mSharedPrefrences.edit()
                        editor.putString(Constants.Weather_Response_Data,weatherJsonResponseString)
                        editor.apply()
                        setupUI()
                        Log.d("Weather Details ", weatherList.toString())
                    } else {
                        Log.e("Weather Details", "Response Code: ${response.code()}")
                        Log.e("Weather Details", "Error Body: ${response.errorBody()?.string()}")
                        when (response.code()) {
                            400 -> Toast.makeText(this@MainActivity, "BAD CONNECTION", Toast.LENGTH_LONG).show()
                            404 -> Toast.makeText(this@MainActivity, "NOT FOUND", Toast.LENGTH_LONG).show()
                            else -> Toast.makeText(this@MainActivity, "GENERIC ERROR", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    hideDialog()
                    Toast.makeText(this@MainActivity, t.message.toString(), Toast.LENGTH_LONG).show()
                }

            })

        } else {
            Toast.makeText(this@MainActivity, "No internet Connection", Toast.LENGTH_LONG).show()
        }
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { location ->
                val latitude = location.latitude
                val longitude = location.longitude
                Log.d("Latitude", latitude.toString())
                Log.d("Longitude", longitude.toString())
                getLocationWeatherDetails(latitude, longitude)
            }
        }
    }

    private fun showDialog() {
        mProgressDialog = Dialog(this)
        mProgressDialog!!.setContentView(R.layout.dialog_custom_progress)
        mProgressDialog!!.show()
    }

    private fun hideDialog() {
        mProgressDialog?.dismiss()
    }

    private fun showRationalDialogPermission() {
        AlertDialog.Builder(this)
            .setMessage("It looks like you turned off the permissions")
            .setPositiveButton("Go to Settings") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI() {
        val wetherResJsonString=mSharedPrefrences.getString(Constants.Weather_Response_Data,"")
        if(!wetherResJsonString.isNullOrEmpty())
        {
            val weatherList=Gson().fromJson(wetherResJsonString,WeatherResponse::class.java)
            for (i in weatherList.weather.indices) {
                Log.d("Weather Name ", weatherList.weather.toString())
                binding?.textWeather?.text = weatherList.weather[i].main
                binding?.textCondition?.text = weatherList.weather[i].description

                binding?.textMinimum?.text = "Min : " + weatherList.main.temp_min.toString() + getUnit(application.resources.configuration.locales.toString())
                binding?.textMaximum?.text = "Max : " + weatherList.main.temp_max.toString() + getUnit(application.resources.configuration.locales.toString())

                binding?.textSunrise?.text = unixTime(weatherList.sys.sunrise) + "AM"
                binding?.textSunset?.text = unixTime(weatherList.sys.sunset) + "PM"

                binding?.textDegree?.text = weatherList.main.temp.toString()
                binding?.textPercent?.text = weatherList.main.humidity.toString() + "/percent"

                binding?.textWind?.text = weatherList.wind.speed.toString() + "mph"

                binding?.textName?.text = weatherList.name
                binding?.textCountry?.text = weatherList.sys.country

                when (weatherList.weather[i].icon) {
                    "01d" -> binding?.imageSnowflake?.setImageResource(R.drawable.sunny)
                    "02d" -> binding?.imageSnowflake?.setImageResource(R.drawable.cloud)
                    "03d" -> binding?.imageSnowflake?.setImageResource(R.drawable.cloud)
                    "04d" -> binding?.imageSnowflake?.setImageResource(R.drawable.cloud)
                    "04n" -> binding?.imageSnowflake?.setImageResource(R.drawable.cloud)
                    "10d" -> binding?.imageSnowflake?.setImageResource(R.drawable.rain)
                    "11d" -> binding?.imageSnowflake?.setImageResource(R.drawable.storm)
                    "13d" -> binding?.imageSnowflake?.setImageResource(R.drawable.snowflake)
                    "01n" -> binding?.imageSnowflake?.setImageResource(R.drawable.cloud)
                    "02n" -> binding?.imageSnowflake?.setImageResource(R.drawable.cloud)
                    "03n" -> binding?.imageSnowflake?.setImageResource(R.drawable.cloud)
                    "10n" -> binding?.imageSnowflake?.setImageResource(R.drawable.cloud)
                    "11n" -> binding?.imageSnowflake?.setImageResource(R.drawable.rain)
                    "13n" -> binding?.imageSnowflake?.setImageResource(R.drawable.snowflake)
                }
        }


        }
    }

    private fun getUnit(value: String): String {
        return if ("US" == value || "LR" == value || "MM" == value) "F" else "C"
    }

    private fun unixTime(timex: Long): String? {
        val date = Date(timex * 1000L)
        val sdf = SimpleDateFormat("HH:mm", Locale.UK)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}
