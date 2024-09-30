package com.example.realimedatabase.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.realimedatabase.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    lateinit var binding:ActivityMainBinding
    val firebase: DatabaseReference =FirebaseDatabase.getInstance().getReference()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.insert.setOnClickListener {
           val intent= Intent(this, activity_insert::class.java)
            startActivity(intent)
        }
        binding.FETCH.setOnClickListener {
            val intent= Intent(this, FetchActivity::class.java)
            startActivity(intent)
        }
    }
}