package msi.crool.happyplaces.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import msi.crool.happyplaces.Models.HappyPlacesModel
import msi.crool.happyplaces.R
import msi.crool.happyplaces.databinding.ActivityHappyPlaceDetailBinding
import msi.crool.happyplaces.databinding.ActivityMainBinding

class HappyPlaceDetail : AppCompatActivity() {
    private var binding:ActivityHappyPlaceDetailBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityHappyPlaceDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        var happyPlaceModel:HappyPlacesModel?=null

        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DEATIL))
        {
            happyPlaceModel=intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DEATIL) as HappyPlacesModel
        }

        binding?.descImg?.setImageURI(Uri.parse(happyPlaceModel?.Image))
        binding?.descTextviewIDOne?.text=happyPlaceModel?.title
        binding?.descTextviewIDTwo?.text=happyPlaceModel?.Description

        binding?.viewMap?.setOnClickListener {
            val intent= Intent(this@HappyPlaceDetail,MapActivity::class.java)
            intent.putExtra(MainActivity.EXTRA_PLACE_DEATIL,happyPlaceModel)
            startActivity(intent)
        }
    }
}