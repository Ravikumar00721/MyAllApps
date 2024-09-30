package msi.crool.drawingapp

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import msi.crool.drawingapp.ui.theme.DrawingAppTheme
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    private var drawingView: DrawingView? = null
    private var mimageButtoncurrentVar: ImageButton? = null
    private var customProgressDialog:Dialog?=null

    var undo_btn:ImageButton?=null

    val opeGalleryLauncher:ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            result->
            if(result.resultCode== RESULT_OK && result.data!=null)
            {
                val imgBck:ImageView=findViewById(R.id.iii)
                imgBck.setImageURI(result.data?.data)
            }
        }

    private val fileRequestLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Permission Granted for File", Toast.LENGTH_LONG).show()
                // Handle your file opening logic here
                val pickIntent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                opeGalleryLauncher.launch(pickIntent)

            } else {
                Toast.makeText(this, "Permission Denied for File", Toast.LENGTH_LONG).show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.draw_2)
        val linearLayout = findViewById<LinearLayout>(R.id.ll_color_pallete)
        mimageButtoncurrentVar = linearLayout[0] as ImageButton
        mimageButtoncurrentVar!!.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.palletepressed)
        )

        val ibBrush: ImageButton = findViewById(R.id.brush)
        ibBrush.setOnClickListener {
            showBrushSizeChooser()
        }

        val img: ImageButton = findViewById(R.id.img)
        img.setOnClickListener {
            requestFilePermission()
        }
        undo_btn=findViewById(R.id.undo)
        undo_btn?.setOnClickListener {
            drawingView?.undoOnClick()
        }

        var redo_btn:ImageButton?=findViewById(R.id.redo)
        redo_btn?.setOnClickListener{
            drawingView?.redoOnClick()
        }

        var save_btn:ImageButton?=findViewById(R.id.Save)
        save_btn?.setOnClickListener{

            if(isReadStoAll())
            {
                showProgressDialog()
                lifecycleScope.launch {
                    val fl:FrameLayout=findViewById(R.id.frame_layout)
                    saveBitMapFile(getBitMapImageView(fl))
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private  fun isReadStoAll():Boolean{
        val result=ContextCompat.checkSelfPermission(this,Manifest.permission.READ_MEDIA_IMAGES)
        return result==PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestFilePermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)) {
            Toast.makeText(
                this,
                "File cannot be used because permission is denied",
                Toast.LENGTH_LONG
            ).show()
        } else {
            fileRequestLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            fileRequestLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun showBrushSizeChooser() {
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.brushview)
        brushDialog.setTitle("Brush Size:")

        val smallBrush: ImageButton = brushDialog.findViewById(R.id.small_brush)
        smallBrush.setOnClickListener {
            drawingView?.setSizeForBrush(10.toFloat())
            brushDialog.dismiss()
        }

        val mediumBrush: ImageButton = brushDialog.findViewById(R.id.medium_brush)
        mediumBrush.setOnClickListener {
            drawingView?.setSizeForBrush(20.toFloat())
            brushDialog.dismiss()
        }

        val largeBrush: ImageButton = brushDialog.findViewById(R.id.large_brush)
        largeBrush.setOnClickListener {
            drawingView?.setSizeForBrush(30.toFloat())
            brushDialog.dismiss()
        }

        brushDialog.show()
    }

    private fun getBitMapImageView(view: View): Bitmap {
        // Step 1: Create a blank bitmap with the same dimensions as the view
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)

        // Step 2: Create a canvas to draw onto the bitmap
        val canvas = Canvas(returnedBitmap)

        // Step 3: Get the view's background drawable
        val bgDrawable = view.background

        // Step 4: Draw the background onto the canvas if it exists, else fill with white color
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }

        // Step 5: Draw the view's content onto the canvas
        view.draw(canvas)

        // Step 6: Return the bitmap that now contains the drawn content of the view
        return returnedBitmap
    }


    private suspend fun saveBitMapFile(mBitmap : Bitmap?):String
    {
       var result=""
        withContext(Dispatchers.IO)
        {
            if(mBitmap!=null)
            {
                try{
                    val bytes=ByteArrayOutputStream()
                    mBitmap.compress(Bitmap.CompressFormat.PNG,90,bytes)

                    val f=File(externalCacheDir?.absoluteFile.toString() + File.separator
                    + "KidsDrawingApp" + System.currentTimeMillis()/1000 +".png"
                    )
                    val fo =FileOutputStream(f)
                    fo.write(bytes.toByteArray())
                    fo.close()

                    result=f.absolutePath

                    runOnUiThread{
                        cancelProgressBar()
                        if(result.isNotEmpty())
                        {
                            Toast.makeText(this@MainActivity,"File Saved Successfully $result",Toast.LENGTH_SHORT).show()
                            shareImage(result = result)
                        }
                        else
                        {
                            Toast.makeText(this@MainActivity,"Something went Wrong $result",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                catch (e:Exception)
                {
                    result=""
                    e.printStackTrace()
                }
            }
        }
        return result
    }

    fun paintClicked(view: View) {
        if (view != mimageButtoncurrentVar) {
            val imageButton = view as ImageButton
            val colorTag = imageButton.tag.toString()
            drawingView?.setColor(colorTag)
            imageButton.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.palletepressed)
            )
            mimageButtoncurrentVar?.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallete)
            )
            mimageButtoncurrentVar = view
        }
    }

    private fun showProgressDialog()
    {
        Log.d("Progress BAR WORKED", "WORKIIIIIIIIIIIIIIIIIIIIIING ")
        customProgressDialog=Dialog(this@MainActivity)
        customProgressDialog?.setContentView(R.layout.dialog_custom)
        customProgressDialog?.show()
    }
    private fun cancelProgressBar()
    {
        if(customProgressDialog!=null)
        {
            customProgressDialog?.dismiss()
            customProgressDialog=null
        }
    }
    private fun shareImage(result:String)
    {
         MediaScannerConnection.scanFile(this, arrayOf(result),null)
         {
             path,uri->
             val sharIntent=Intent()
             sharIntent.action=Intent.ACTION_SEND
             sharIntent.putExtra(Intent.EXTRA_STREAM,uri)
             sharIntent.type="image/png"
             startActivity(Intent.createChooser(sharIntent,"share"))
         }
    }
}
