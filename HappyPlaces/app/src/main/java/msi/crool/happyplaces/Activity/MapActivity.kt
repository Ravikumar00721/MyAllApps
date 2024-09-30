package msi.crool.happyplaces.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import msi.crool.happyplaces.Models.HappyPlacesModel
import msi.crool.happyplaces.R

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mHappyPlaceDetails: HappyPlacesModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DEATIL)) {
            mHappyPlaceDetails = intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DEATIL) as? HappyPlacesModel
        }

        val supportMapFrag: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.MAP) as SupportMapFragment
        supportMapFrag.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mHappyPlaceDetails?.let { placeDetails ->
            val position = LatLng(placeDetails.Lattitude!!, placeDetails.Longitude!!)
            googleMap.addMarker(
                MarkerOptions()
                    .position(position)
                    .title("Marker in ${placeDetails.title}") // Adjust title if needed
            )
            googleMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLng(position))
        }
    }
}
