package msi.crool.locationapp

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalContext

import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import msi.crool.locationapp.ui.theme.LocationAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel:LocationViewModel=viewModel()
            LocationAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                      MyApp(viewModel)
                }
            }
        }
    }
}
@Composable
fun MyApp(viewModel: LocationViewModel)
{
    val context= LocalContext.current
    val locationUtils=LocationUtil(context)
    LocationDisplay(locationUtils = locationUtils,viewModel=viewModel, context =context )
}


@Composable
fun LocationDisplay(
    locationUtils:LocationUtil,
    context:Context,
    viewModel:LocationViewModel)
{
    val location=viewModel.location.value
    val address=location?.let{
        locationUtils.reverseGeocodelocation(location)//calling
    }
    val reqPerLaun= rememberLauncherForActivityResult(contract =
        ActivityResultContracts.RequestMultiplePermissions()
        , onResult =
        {
            permission->
            if(permission[Manifest.permission.ACCESS_FINE_LOCATION]==true&&
                permission[Manifest.permission.ACCESS_COARSE_LOCATION]==true)
            {
                //i have access to location
                locationUtils.requestLocationObjects(viewModel=viewModel)
            }
            else
            {
                //ask for location
                val rationaleRequired=ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )||ActivityCompat.shouldShowRequestPermissionRationale(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )

                if(rationaleRequired)
                {
                    Toast.makeText(context,
                        "LocationPermission Required ",Toast.LENGTH_LONG)
                        .show()
                }else
                {
                    Toast.makeText(context,
                        "LocationPermission Required :Pls Enable in Android Settings ",Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    )
    Column(
        modifier= Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if(location!=null)
        {
            Text(text = "Address : ${location.latitude} ${location.longitude}  \n  ${address}")
        }
        else
        {
            Text(text = "Location Not Available")
        }


        Button(onClick = {
            if(locationUtils.hasLocationPermission(context))
            {
               locationUtils.requestLocationObjects(viewModel = viewModel)
            }
            else
            {
               reqPerLaun.launch(
                   arrayOf(
                       Manifest.permission.ACCESS_FINE_LOCATION,
                       Manifest.permission.ACCESS_COARSE_LOCATION
                   )
               )
            }
        }) {
            Text(text = "Get Location")
        }
    }
}