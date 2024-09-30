package msi.crool.camerapermission

import android.Manifest
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import msi.crool.camerapermission.R

class MainActivity : ComponentActivity() {

    private val cameraResultLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Permission Granted for camera", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_LONG).show()
            }
        }

    private val cameraResultLauncherAndLocation: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted) {
                    if (permissionName == Manifest.permission.ACCESS_FINE_LOCATION) {
                        Toast.makeText(this, "Permission Granted for Location", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Permission Granted for Camera", Toast.LENGTH_LONG).show()
                    }
                } else {
                    if (permissionName == Manifest.permission.ACCESS_FINE_LOCATION) {
                        Toast.makeText(this, "Permission Denied for Location", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Permission Denied for Camera", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Make sure you have activity_main.xml with the correct layout

        val requestBtn: Button = findViewById(R.id.ufo) // Ensure R.id.ufo exists in your activity_main.xml
        requestBtn.setOnClickListener {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Toast.makeText(this, "Camera cannot be used because permission is denied", Toast.LENGTH_LONG).show()
            } else {
                cameraResultLauncherAndLocation.launch(
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
                )
            }
        }
    }
}
