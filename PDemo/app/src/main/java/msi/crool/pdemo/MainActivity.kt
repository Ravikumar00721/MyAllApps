package msi.crool.pdemo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import msi.crool.pdemo.databinding.ActivityMainBinding
import msi.crool.pdemo.ui.theme.PDemoTheme
import android.Manifest
import android.app.AlertDialog
import android.os.Build

class MainActivity : ComponentActivity() {
    private var binding:ActivityMainBinding?=null

    private val cameraResultLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Permission granted for Camera", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission denied for Camera", Toast.LENGTH_LONG).show()
            }
        }
    private val cameraAndLocationResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach { entry ->
                val permissionName = entry.key
                val isGranted = entry.value
                if (isGranted) {
                    when (permissionName) {
                        Manifest.permission.ACCESS_FINE_LOCATION ->
                            Toast.makeText(this, "Permission granted for Fine Location", Toast.LENGTH_LONG).show()
                        Manifest.permission.ACCESS_COARSE_LOCATION ->
                            Toast.makeText(this, "Permission granted for Coarse Location", Toast.LENGTH_LONG).show()
                        Manifest.permission.CAMERA ->
                            Toast.makeText(this, "Permission granted for Camera", Toast.LENGTH_LONG).show()
                    }
                } else {
                    when (permissionName) {
                        Manifest.permission.ACCESS_FINE_LOCATION ->
                            Toast.makeText(this, "Permission denied for Fine Location", Toast.LENGTH_LONG).show()
                        Manifest.permission.ACCESS_COARSE_LOCATION ->
                            Toast.makeText(this, "Permission denied for Coarse Location", Toast.LENGTH_LONG).show()
                        Manifest.permission.CAMERA ->
                            Toast.makeText(this, "Permission denied for Camera", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.PDEMO?.setOnClickListener {
            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
            {
               showRationaleDialog("Permission Demo Requires Camera Access","Camera cannot be used because camera access is denied")
            }else
            {
                cameraAndLocationResultLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CAMERA))
            }
        }
    }

    private fun showRationaleDialog(title:String,Meassage:String)
    {
        val dd= AlertDialog.Builder(this)
        dd.setTitle(title)
        dd.setMessage(Meassage)
        dd.setIcon(R.drawable.r_icon)
        dd.setNeutralButton("Cancel")
        {
            dialog,_->run{
                dialog.dismiss()
        }
        }
        val kb:AlertDialog=dd.create()
        kb.setCancelable(false)
        kb.show()
    }
}

