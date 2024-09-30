package com.example.imagepickerai

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.imagepickerai.databinding.ActivityMainBinding
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class MainActivity : ComponentActivity(), ObjectDetectorHelper.DetectorListener {
    private var binding: ActivityMainBinding? = null
    lateinit var objectDetectorHelper: ObjectDetectorHelper
    private var imageUri: Uri? = null

    companion object {
        private const val MEDIA_PICK = 1
        private const val CAMERA_CAPTURE = 2
        private const val CAMERA_PER_CODE = 3
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        // Click listener for picking an image from the gallery
        binding?.btnCamera?.setOnClickListener {
            requestGalleryPermission()
        }

        // Click listener for capturing an image from the camera
        binding?.btnCameraPick?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                captureImageFromCamera()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PER_CODE
                )
            }
        }

        objectDetectorHelper = ObjectDetectorHelper(
            threshold = 0.5f,
            maxResults = ObjectDetectorHelper.MAX_RESULTS_DEFAULT,
            currentDelegate = ObjectDetectorHelper.DELEGATE_CPU,
            modelName = "fruits.tflite",
            runningMode = RunningMode.IMAGE,
            context = applicationContext,
            objectDetectorListener = this
        )
    }

    // Function to request gallery permissions and open the gallery
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestGalleryPermission() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_MEDIA_IMAGES
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report != null && report.areAllPermissionsGranted()) {
                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(intent, MEDIA_PICK)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    // Function to capture an image from the camera
    private fun captureImageFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_CAPTURE)
    }

    // Handle permission result for the camera
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PER_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            captureImageFromCamera()
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
        }
    }

    // Handle activity result for both gallery and camera
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                MEDIA_PICK -> {
                    // Handle image picked from gallery
                    imageUri = data?.data
                    imageUri?.let {
                        binding?.imageId?.setImageURI(it)
                        doInference(it)
                    }
                }
                CAMERA_CAPTURE -> {
                    // Handle image captured from camera
                    val bitmap = data?.extras?.get("data") as? Bitmap
                    bitmap?.let {
                        binding?.imageId?.setImageBitmap(it)
                        doInferenceFromBitmap(it)
                    }
                }
            }
        } else {
            Toast.makeText(this, "Image capture failed", Toast.LENGTH_SHORT).show()
        }
    }

    // Perform inference on the selected image from gallery
    private fun doInference(uri: Uri) {
        val bitmap = uriToBitmap(uri)
        bitmap?.let {
            val resultBundle = objectDetectorHelper.detectImage(it)
            resultBundle?.let { bundle ->
                Log.d("TryRes", bundle.results.toString())
            }
        }
    }

    // Perform inference on the image captured from the camera
    private fun doInferenceFromBitmap(bitmap: Bitmap) {
        val resultBundle = objectDetectorHelper.detectImage(bitmap)
        resultBundle?.let {
            Log.d("TryRes", it.results.toString())
        }
    }

    // Convert URI to Bitmap
    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedFileUri, "r")
        val fileDescriptor = parcelFileDescriptor?.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor?.close()
        return image
    }

    override fun onError(error: String, errorCode: Int) {
        Log.e("ObjectDetectionError", "Error: $error, Code: $errorCode")
    }

    override fun onResults(resultBundle: ObjectDetectorHelper.ResultBundle) {
        Log.d("DetectionResults", resultBundle.results.toString())
    }
}
