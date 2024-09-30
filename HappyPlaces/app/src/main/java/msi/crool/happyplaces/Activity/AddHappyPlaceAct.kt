package msi.crool.happyplaces.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Notification.Action
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.ContactsContract.RawContacts.Data
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.PermissionRequest
import msi.crool.happyplaces.Databases.DatabaseHandler
import msi.crool.happyplaces.Models.HappyPlacesModel
import msi.crool.happyplaces.R
import msi.crool.happyplaces.databinding.ActivityAddHappyPlaceBinding
import msi.crool.happyplaces.utils.GetAddressFromLatLng
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.UUID

class AddHappyPlaceAct : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityAddHappyPlaceBinding? = null
    private val cal = Calendar.getInstance()
    private lateinit var datePickerDialog: DatePickerDialog.OnDateSetListener

    private var saveImageToInter:Uri?=null
    private var mLatitude:Double?=0.0
    private var mLongitude:Double?=0.0

    private var mHappyPlaceDeatils : HappyPlacesModel?=null

    private lateinit var mFusedLoactionClient:FusedLocationProviderClient

    private var Loca:EditText?=null

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHappyPlaceBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbar)

        Loca=findViewById(R.id.location_id)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.toolbar?.setNavigationOnClickListener {
            onBackPressed()
        }

        mFusedLoactionClient=LocationServices.getFusedLocationProviderClient(this)
        if(!Places.isInitialized())
        {
            Places.initialize(this@AddHappyPlaceAct,resources.getString(R.string.GoogleMapAPI))
        }

        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DEATIL))
        {
            mHappyPlaceDeatils=intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DEATIL) as HappyPlacesModel
        }

        datePickerDialog = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateInView()
        }

        if(mHappyPlaceDeatils!=null)
        {
            supportActionBar?.title="Edit Happy Place"

            binding?.titleId?.setText(mHappyPlaceDeatils!!.title)
            binding?.descriptionId?.setText(mHappyPlaceDeatils!!.Description)
            binding?.dateId?.setText(mHappyPlaceDeatils!!.Date)
            binding?.locationId?.setText(mHappyPlaceDeatils!!.Location)
            mLatitude=mHappyPlaceDeatils!!.Lattitude
            mLongitude=mHappyPlaceDeatils!!.Longitude

            saveImageToInter=Uri.parse(mHappyPlaceDeatils!!.Image)

            binding?.image?.setImageURI(saveImageToInter)

            binding?.saveBtn?.text="UPDATE"
        }

        binding?.dateId?.setOnClickListener(this)
        binding?.imgBtn?.setOnClickListener(this)
        binding?.saveBtn?.setOnClickListener(this)
        binding?.locationId?.setOnClickListener(this)
        binding?.SelectCurr?.setOnClickListener(this)

    }

    @SuppressLint("MissingPermission")
    private fun reqNewLocationData()
    {
        var mLocReq= com.google.android.gms.location.LocationRequest()
        mLocReq.priority=LocationRequest.QUALITY_HIGH_ACCURACY
        mLocReq.interval=1000
        mLocReq.numUpdates=1

        mFusedLoactionClient.requestLocationUpdates(mLocReq,mLocationCallback, Looper.myLooper())
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            val lastLocation: Location? = locationResult.lastLocation
            lastLocation?.let { location ->
                mLatitude = location.latitude
                mLongitude = location.longitude

                val addressTask = GetAddressFromLatLng(this@AddHappyPlaceAct, mLatitude!!, mLongitude!!)
                addressTask.setAddressListener(object : GetAddressFromLatLng.AddressListener {
                    override fun onAddressFound(address: String?) {
                        val addressText = address ?: "No address found"
                        Loca?.setText(addressText)
                        Log.d("GetAddress", "Address found: $addressText")
                    }

                    override fun onError() {
                        Log.e("GetAddress", "Error retrieving address")
                    }
                })
                addressTask.getAddress()
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.date_id -> {
                DatePickerDialog(
                    this@AddHappyPlaceAct,
                    datePickerDialog, cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            R.id.img_btn -> {
                val pictureDialog = AlertDialog.Builder(this)
                pictureDialog.setTitle("Select Action")
                val picDItems = arrayOf("Select Photo From Gallery", "Capture Photo From Camera")
                pictureDialog.setItems(picDItems) { _, which ->
                    when (which) {
                        0 -> choosePhotoFromGallery()
                        1 -> takePhotoFromCAmera()
                    }
                }
                pictureDialog.show()
            }
            R.id.save_btn->{
                when{
                    binding?.titleId?.text?.isEmpty() == true ->{
                        Toast.makeText(this@AddHappyPlaceAct,"Please Enter Title",Toast.LENGTH_LONG).show()
                    }
                    binding?.descriptionId?.text?.isEmpty() == true ->{
                        Toast.makeText(this@AddHappyPlaceAct,"Please Enter Description",Toast.LENGTH_LONG).show()
                    }
                    binding?.locationId?.text?.isEmpty() == true ->{
                        Toast.makeText(this@AddHappyPlaceAct,"Please Enter Location",Toast.LENGTH_LONG).show()
                    }
                    saveImageToInter==null->{
                        Toast.makeText(this@AddHappyPlaceAct,"Please Select an Image",Toast.LENGTH_LONG).show()
                    }
                    else->{
                        val happyPlacesModel=HappyPlacesModel(
                            id = if(mHappyPlaceDeatils==null) 0 else mHappyPlaceDeatils!!.id,
                            binding?.titleId?.text.toString(),
                            saveImageToInter.toString(),
                            binding?.descriptionId?.text.toString(),
                            binding?.dateId?.text.toString(),
                            binding?.locationId?.text.toString(),
                            mLatitude,
                            mLongitude
                        )
                        val dbHandler=DatabaseHandler(this)
                        if(mHappyPlaceDeatils==null)
                        {
                            val addHappyPlace=dbHandler.addHappyPlace(happyPlacesModel)
                            if(addHappyPlace>0)
                            {
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        }else
                        {
                            val updateHappyPlace=dbHandler.updateHappyPlace(happyPlacesModel)
                            if(updateHappyPlace>0)
                            {
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        }
                    }
                }
            }
            R.id.location_id->{
                try {
                    val fields= listOf(
                        Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS
                    )
                    val intent= Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,fields).build(this@AddHappyPlaceAct)
                    startActivityForResult(intent, PLACE_AUTOCOMPLETEREQ_CODE)
                }catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }
            R.id.Select_curr -> {
                Log.d("LOCATION", "Select_curr button clicked")

                if (!isLocationEnabled()) {
                    Log.d("LOCATION", "Location provider is off")
                    Toast.makeText(this@AddHappyPlaceAct, "YOUR LOCATION PROVIDER IS TURNED OFF", Toast.LENGTH_LONG).show()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                } else {
                    Log.d("LOCATION", "Location provider is on, requesting permissions")
                    Dexter.withContext(this)
                        .withPermissions(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        .withListener(object : MultiplePermissionsListener {
                            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                                if (report?.areAllPermissionsGranted() == true) {
                                    reqNewLocationData()
                                } else {
                                    Log.d("LOCATION", "Permissions not granted")
                                }
                            }

                            override fun onPermissionRationaleShouldBeShown(
                                permissions: MutableList<PermissionRequest>?,
                                token: PermissionToken?
                            ) {
                                Log.d("LOCATION", "Permissions rationale should be shown")
                                showRationaleDialogPerm()
                            }
                        }).onSameThread().check()
                }
            }

        }
    }

    private fun isLocationEnabled():Boolean{
        val locationManager: LocationManager? = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun takePhotoFromCAmera() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.CAMERA)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report?.areAllPermissionsGranted() == true) {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(intent, CAMERA_REQUEST_CODE)
                    } else {
                        Toast.makeText(this@AddHappyPlaceAct, "Permission Denied", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    showRationaleDialogPerm()
                }
            })
            .onSameThread()
            .check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val thumbnail: Bitmap? = data?.extras?.get("data") as? Bitmap
            if (thumbnail != null) {
                saveImageToInter = saveImagetoDevice(thumbnail)
                Log.d("CAMERATIME", "SAVED :: $saveImageToInter")
                binding?.image?.setImageBitmap(thumbnail)
            }
        } else if (requestCode == PLACE_AUTOCOMPLETEREQ_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                try {
                    val place: Place = Autocomplete.getPlaceFromIntent(it)
                    binding?.locationId?.setText(place.address)
                    mLongitude = place.latLng?.longitude
                    mLatitude = place.latLng?.latitude
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error retrieving place details", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun choosePhotoFromGallery() {
        Dexter.withContext(this)
            .withPermissions(Manifest.permission.READ_MEDIA_IMAGES)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report?.areAllPermissionsGranted() == true) {
                        Log.d("PERMISSION", "NOT DENIED")
                        openGallery()
                    } else {
                        Toast.makeText(this@AddHappyPlaceAct, "Permission Denied", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    showRationaleDialogPerm()
                }
            })
            .onSameThread()
            .check()
    }


    private fun showRationaleDialogPerm() {
        AlertDialog.Builder(this)
            .setMessage("It looks like you turned off some permissions. Please enable them in app settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { contentURI ->
            try {
                val inputStream = contentResolver.openInputStream(contentURI)
                val selectedImageBitmap = BitmapFactory.decodeStream(inputStream)
                saveImageToInter=saveImagetoDevice(selectedImageBitmap)
                Log.d("GALLERYTIME", "SAVED :: $saveImageToInter")
                binding?.image?.setImageBitmap(selectedImageBitmap)
            } catch (e: Exception) {
                Log.d("IMAGE_SELECTION_ERROR", "Error loading image", e)
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Log.d("IMAGE_SELECTION_ERROR", "Image data is null")
            Toast.makeText(this, "Image data is null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        try {
            Log.d("IMAGE_SELECTION_Done", "opening gallery")
            getImage.launch("image/*")
        } catch (e: Exception) {
            Log.d("IMAGE_SELECTION_ERROR", "Error opening gallery", e)
            Toast.makeText(this, "Error opening gallery", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateInView() {
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, java.util.Locale.getDefault())
        binding?.dateId?.setText(sdf.format(cal.time).toString())
    }

    private fun saveImagetoDevice(bitmap: Bitmap):Uri{
        val wrapper=ContextWrapper(applicationContext)
        var file=wrapper.getDir(IMAGE_DIR,Context.MODE_PRIVATE)
        file= File(file,"${UUID.randomUUID()}.jpg")

        try {
            val stream:OutputStream=FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }catch (e:Exception)
        {
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 1
        private const val CAMERA_REQUEST_CODE=2
        private const val  IMAGE_DIR="HAPPYPLACESIMAGE"
        private const val PLACE_AUTOCOMPLETEREQ_CODE=3
    }
}
