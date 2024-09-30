package msi.crool.googlemapapi

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import msi.crool.googlemapapi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    //When you use Google Maps in your Android application, the map is loaded asynchronously.
    // This means that the map might not be ready immediately after you request it.
    // To handle this, you implement the OnMapReadyCallback interface,
    // which provides a callback method that is invoked when the map is fully initialized and ready for use.
    private var binding: ActivityMainBinding? = null
    private lateinit var googleMap: GoogleMap

    companion object {
        private const val AUTOCOMPLETE_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (!Places.isInitialized()) {
            Places.initialize(this@MainActivity, resources.getString(R.string.API_KEY))
        }

        binding?.EnterLocation?.setOnClickListener {
            val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

        binding?.btnMap?.setOnClickListener {
            val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment //just correct this
            if (mapFragment != null) {
                mapFragment.getMapAsync(this)
            } else {
                Log.e("MainActivity", "SupportMapFragment is null")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                binding?.btnTextView?.text = place.name
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                Log.e("MainActivity", status.statusMessage ?: "Error occurred")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-34.0, 151.0), 10f))

        googleMap.setOnMapClickListener { latLng ->
            binding?.btnTextView?.text = "Lat: ${latLng.latitude}, Lng: ${latLng.longitude}"
        }
    }
}

