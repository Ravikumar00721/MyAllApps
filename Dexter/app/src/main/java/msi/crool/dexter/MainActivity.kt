package msi.crool.dexter

import android.Manifest
import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import msi.crool.dexter.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.PDEMO?.setOnClickListener {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: com.karumi.dexter.MultiplePermissionsReport) {
                    // Check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        Toast.makeText(this@MainActivity, "All permissions are granted!", Toast.LENGTH_LONG).show()
                    }

                    // Check for any denied permissions
                    if (report.isAnyPermissionPermanentlyDenied) {
                        showRationaleDialog(
                            "Permissions Denied",
                            "Some permissions are permanently denied. Please enable them from the app settings."
                        )
                    } else {
                        report.deniedPermissionResponses.forEach { response ->
                            when (response.permissionName) {
                                Manifest.permission.ACCESS_FINE_LOCATION ->
                                    Toast.makeText(this@MainActivity, "Permission denied for Fine Location", Toast.LENGTH_LONG).show()
                                Manifest.permission.ACCESS_COARSE_LOCATION ->
                                    Toast.makeText(this@MainActivity, "Permission denied for Coarse Location", Toast.LENGTH_LONG).show()
                                Manifest.permission.CAMERA ->
                                    Toast.makeText(this@MainActivity, "Permission denied for Camera", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                    showRationaleDialog(
                        "Permission Required",
                        "This app needs access to Camera and Location to function properly."
                    )
                    token?.continuePermissionRequest()
                }
            }).check()
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
