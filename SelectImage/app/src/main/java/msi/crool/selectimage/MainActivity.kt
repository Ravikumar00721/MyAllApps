package msi.crool.selectimage

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import msi.crool.selectimage.databinding.ActivityMainBinding
import java.io.OutputStream

class MainActivity : ComponentActivity() {
    private var binding: ActivityMainBinding? = null
    private var selectedImageUri: Uri? = null

    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                selectedImageUri = result.data?.data
                val img: ImageView? = binding?.imageView
                img?.setImageURI(selectedImageUri)
            }
        }

    private val permissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach { entry ->
                val permissionName = entry.key
                val isGranted = entry.value
                if (isGranted) {
                    when (permissionName) {
                        Manifest.permission.READ_MEDIA_IMAGES ->
                            Toast.makeText(this, "Permission granted for reading external storage", Toast.LENGTH_LONG).show()
                        Manifest.permission.WRITE_EXTERNAL_STORAGE ->
                            Toast.makeText(this, "Permission granted for writing to external storage", Toast.LENGTH_LONG).show()
                    }
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGalleryLauncher.launch(intent)
                } else {
                    when (permissionName) {
                        Manifest.permission.READ_MEDIA_IMAGES ->
                            Toast.makeText(this, "Permission denied for reading external storage", Toast.LENGTH_LONG).show()
                        Manifest.permission.WRITE_EXTERNAL_STORAGE ->
                            Toast.makeText(this, "Permission denied for writing to external storage", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        // Select Image Button
        binding?.btn1?.setOnClickListener {
            when {
                shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES) -> {
                    showRationaleDialog(
                        "Permission Requires Access to External Storage",
                        "External storage cannot be read because access is denied."
                    )
                }
                shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                    showRationaleDialog(
                        "Permission Requires Writing to External Storage",
                        "External storage cannot be written to because access is denied."
                    )
                }
                else -> {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    )
                }
            }
        }

        // Save Image Button
        binding?.btn2?.setOnClickListener {
            selectedImageUri?.let {
                saveImageToExternalStorage(it)
            } ?: Toast.makeText(this, "No image selected to save", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageToExternalStorage(uri: Uri) {
        val contentResolver = contentResolver
        val imageCollection =
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "SelectedImage_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val imageUri = contentResolver.insert(imageCollection, imageDetails)

        imageUri?.let {
            contentResolver.openOutputStream(it).use { outputStream ->
                saveContentToFile(uri, outputStream)
            }
        }
    }

    private fun saveContentToFile(uri: Uri, outputStream: OutputStream?) {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            outputStream?.use { outStream ->
                inputStream.copyTo(outStream)
            }
        }
        Toast.makeText(this, "Image saved successfully", Toast.LENGTH_LONG).show()
    }

    private fun showRationaleDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setIcon(R.drawable.r_icon)
        builder.setNeutralButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}
