package com.smartherd.globofly.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smartherd.globofly.helpers.DestinationAdapter
import com.smartherd.globofly.helpers.SampleData
import com.smartherd.globofly.models.Destination
import msi.crool.globofly.Services.DestinationServices
import msi.crool.globofly.Services.ServiceBuilder
import msi.crool.globofly.databinding.ActivityDestinyListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DestinationListActivity : AppCompatActivity() {

	private lateinit var binding: ActivityDestinyListBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityDestinyListBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setSupportActionBar(binding.toolbar)
		binding.toolbar.title = title

		binding.fab.setOnClickListener {
			val intent = Intent(this@DestinationListActivity, DestinationCreateActivity::class.java)
			startActivity(intent)
		}
	}

	override fun onResume() {
		super.onResume()
		loadDestinations()
	}

	private fun loadDestinations() {
		// To be replaced by retrofit code
		val destinationServices:DestinationServices=ServiceBuilder.buildService(DestinationServices::class.java)
		val filter=HashMap<String,String>()
//		filter["country"] = "India"
//		filter["count"] = "2"
		val requestCall:Call<List<Destination>> = destinationServices.getDestinationList(filter,"EN")

		requestCall.enqueue(object :Callback<List<Destination>>{
			override fun onResponse(
				call: Call<List<Destination>>,
				response: Response<List<Destination>>
			) {
				if(response.isSuccessful)
				{
					val destibnationList:List<Destination> = response.body()!!
					Log.d("DestinationList", destibnationList.toString())
					binding.destinyRecyclerView.adapter = DestinationAdapter(destibnationList)
				}else
				{
					Toast.makeText(this@DestinationListActivity,"Failed 1", Toast.LENGTH_SHORT).show()
				}
			}

			override fun onFailure(call: Call<List<Destination>>, t: Throwable) {
				Toast.makeText(this@DestinationListActivity,"Failed 2", Toast.LENGTH_SHORT).show()
			}

		})
	}
}
