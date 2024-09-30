package com.example.bitmapimage

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider
import com.example.bitmapimage.databinding.ActivityMainBinding
import com.example.bitmapimage.ui.theme.BitmapImageTheme
import java.io.File

class MainActivity : ComponentActivity() {
    private var binding:ActivityMainBinding?=null
    lateinit var imageuir:Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val container=binding?.view
        imageuir= createImageUri()!!


        binding?.buttn?.setOnClickListener {
            it->
            it.visibility=View.GONE
            val bitmap=getBitMapImage(container!!)
            storeBitmap(bitmap)
        }
    }

    private fun storeBitmap(bitmap: Bitmap) {
        val outputStream=applicationContext.contentResolver.openOutputStream(imageuir)
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream!!)
        Log.d("Saved","Image Saved")
        outputStream.close()
    }

    private fun createImageUri(): Uri? {
        // Get the external files directory for pictures
        val imagesDir = File(applicationContext.getExternalFilesDir(null), "Pictures")

        // Check if the directory exists, if not, create it
        if (!imagesDir.exists()) {
            imagesDir.mkdirs() // Create the directory
        }

        // Create a new file for the image
        val imageFile = File(imagesDir, "camera_photos.png")

        // Return the URI for the file using FileProvider
        return FileProvider.getUriForFile(
            applicationContext,
            "com.example.bitmapimage.fileprovider",  // Ensure this matches your manifest authority
            imageFile
        )
    }

    private fun getBitMapImage(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)

        // Check if the background is not null before drawing it
        val bg = view.background
        if (bg != null) {
            bg.draw(canvas)  // Draw the background only if it's not null
        } else {
            canvas.drawColor(android.graphics.Color.WHITE)  // Optionally, set a default background color (white)
        }

        view.draw(canvas)  // Draw the view contents
        return bitmap
    }

}

