package com.smartherd.globofly.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle

import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils

import com.smartherd.globofly.helpers.SampleData
import com.smartherd.globofly.models.Destination
import msi.crool.globofly.Services.DestinationServices
import msi.crool.globofly.Services.ServiceBuilder
import msi.crool.globofly.databinding.ActivityDestinyDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DestinationDetailActivity : AppCompatActivity() {

	private lateinit var binding: ActivityDestinyDetailBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityDestinyDetailBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setSupportActionBar(binding.detailToolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		val bundle: Bundle? = intent.extras

		bundle?.let {
			if (it.containsKey(ARG_ITEM_ID)) {
				val id = intent.getIntExtra(ARG_ITEM_ID, 0)
				loadDetails(id)
				initUpdateButton(id)
				initDeleteButton(id)
			}
		}
	}

	private fun loadDetails(id: Int) {

		val destinationService:DestinationServices=ServiceBuilder.buildService(DestinationServices::class.java)
		val requestCall: Call<Destination> = destinationService.getDestination(id)

		requestCall.enqueue(object : Callback<Destination>{
			@SuppressLint("SuspiciousIndentation")
			override fun onResponse(call: Call<Destination>, response: Response<Destination>) {
				if(response.isSuccessful)
				{
					val destination = response.body()
		                destination?.let {
							binding.etCity.setText(destination.city)
			                binding.etDescription.setText(destination.description)
							binding.etCountry.setText(destination.country)
			                binding.collapsingToolbar.title = destination.city
		}
				}else
				{
					Toast.makeText(this@DestinationDetailActivity,"Failed",Toast.LENGTH_SHORT).show()
				}
			}

			override fun onFailure(call: Call<Destination>, t: Throwable) {
				Toast.makeText(this@DestinationDetailActivity,"Failed",Toast.LENGTH_SHORT).show()
			}

		})
	}

	private fun initUpdateButton(id: Int) {
		binding.btnUpdate.setOnClickListener {
			val city = binding.etCity.text.toString()
			val description = binding.etDescription.text.toString()
			val country = binding.etCountry.text.toString()


			val destinationService:DestinationServices=ServiceBuilder.buildService(DestinationServices::class.java)
			val requestCall:Call<Destination> =destinationService.updateDestination(id,city,description,country)

			requestCall.enqueue(object :Callback<Destination>{
				override fun onResponse(call: Call<Destination>, response: Response<Destination>) {
					if(response.isSuccessful)
					{
						val destin:Destination?=response.body()
						finish()
					}
				}

				override fun onFailure(call: Call<Destination>, t: Throwable) {
					TODO("Not yet implemented")
				}

			})
		}
	}

	private fun initDeleteButton(id: Int) {
		binding.btnDelete.setOnClickListener {
			val destinationService:DestinationServices=ServiceBuilder.buildService(DestinationServices::class.java)
			val requestCall:Call<Unit> =destinationService.deleteDestination(id)
			requestCall.enqueue(object :Callback<Unit>{
				override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
					if(response.isSuccessful)
					{
						finish()
					}
				}

				override fun onFailure(call: Call<Unit>, t: Throwable) {
					TODO("Not yet implemented")
				}


			})
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return if (item.itemId == android.R.id.home) {
			NavUtils.navigateUpFromSameTask(this)
			true
		} else {
			super.onOptionsItemSelected(item)
		}
	}

	companion object {
		const val ARG_ITEM_ID = "item_id"
	}
}

