package msi.crool.removeactionbar

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import msi.crool.removeactionbar.databinding.ActivityMainBinding
import msi.crool.removeactionbar.databinding.ActivityToolBarBinding

class ToolBarAct : AppCompatActivity() {
    private var bind:ActivityToolBarBinding?=null
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        bind=ActivityToolBarBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(bind?.root)
        setSupportActionBar(bind?.actionbar)

        // Enable the back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Handle back button click
        bind?.actionbar?.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}