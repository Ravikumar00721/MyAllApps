package com.smartherd.globofly.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smartherd.globofly.helpers.DestinationAdapter
import com.smartherd.globofly.helpers.SampleData
import com.smartherd.globofly.models.Destination
import msi.crool.globofly.Services.DestinationServices
import msi.crool.globofly.Services.ServiceBuilder
import msi.crool.globofly.databinding.ActivityDestinyCreateBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DestinationCreateActivity : AppCompatActivity() {

	private lateinit var binding: ActivityDestinyCreateBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityDestinyCreateBinding.inflate(layoutInflater)
		setContentView(binding.root)  // Make sure to set the correct content view

		// Set up toolbar
		setSupportActionBar(binding.toolbar)  // Here, use binding.toolbar instead of R.id.toolbar
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		// Set up the button click listener
		binding.btnAdd.setOnClickListener {
			val newDestination = Destination().apply {
				city = binding.etCity.text.toString()
				description = binding.etDescription.text.toString()
				country = binding.etCountry.text.toString()
			}

			val destinationServices: DestinationServices =
				ServiceBuilder.buildService(DestinationServices::class.java)
			val requestCall: Call<Destination> = destinationServices.addDestination(newDestination)

			requestCall.enqueue(object : Callback<Destination>{
				override fun onResponse(call: Call<Destination>, response: Response<Destination>) {
					if(response.isSuccessful)
					{
						finish()
						val newlyDes:Destination?=response.body()
					}
				}
				override fun onFailure(call: Call<Destination>, t: Throwable) {
				}
			})
		}
	}
}
