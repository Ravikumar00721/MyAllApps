package com.smartherd.globofly.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import msi.crool.globofly.Services.ServiceBuilder
import msi.crool.globofly.Services.anotherService
import msi.crool.globofly.databinding.ActivityWelcomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WelcomeActivity : AppCompatActivity() {

	private lateinit var binding: ActivityWelcomeBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityWelcomeBinding.inflate(layoutInflater)
		setContentView(binding.root)

		// To be replaced by retrofit code
		val messageService:anotherService=ServiceBuilder.buildService(anotherService::class.java)
		val requestCall: Call<String> =messageService.getURL("http://10.0.2.2:9000/messages")
		requestCall.enqueue(object :Callback<String>{
			override fun onResponse(call: Call<String>, response: Response<String>) {
				if(response.isSuccessful)
				{
					val msg:String?=response.body()
					msg?.let {
						binding.message.text = msg
					}
				}
			}

			override fun onFailure(call: Call<String>, t: Throwable) {

			}

		})

	}

	fun getStarted(view: View) {
		val intent = Intent(this, DestinationListActivity::class.java)
		startActivity(intent)
		finish()
	}
}
