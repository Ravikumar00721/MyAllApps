package com.example.firebasestorage

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.firebasestorage.databinding.ActivityMainBinding
import com.google.firebase.storage.FirebaseStorage

class MainActivity : ComponentActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var mImage: Uri
    private val firebaseStorage = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.img.setOnClickListener {
            requestForPermission()
        }
        binding.btn.setOnClickListener {
            imageUpload("image-${System.currentTimeMillis()}.png")
        }
    }

    @SuppressLint("InlinedApi")
    private fun requestForPermission() {
        val imageReq = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED

        if (imageReq) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                IMAGE_PICK
            )
        }
    }

    private fun imageUpload(filename: String) {
        try {
            mImage.let { uri ->
                firebaseStorage.child("images/$filename").putFile(uri)
                    .addOnSuccessListener {
                      Toast.makeText(this,"Image Uploaded",Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exception ->
                        exception.printStackTrace()
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK && resultCode == RESULT_OK) {
            data?.data?.let { imageUri ->
                mImage = imageUri
                binding.img.setImageURI(imageUri)
            }
        }
    }

    companion object {
        const val IMAGE_PICK = 1
    }
}
