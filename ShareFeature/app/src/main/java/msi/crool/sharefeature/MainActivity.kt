package msi.crool.sharefeature

import android.content.Intent
import android.media.MediaScannerConnection
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import msi.crool.sharefeature.ui.theme.ShareFeatureTheme

class MainActivity : ComponentActivity() {
    private  var click:Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        click=findViewById(R.id.share)
        click?.setOnClickListener {
            shareImage("Hello")
        }
    }
    private fun shareImage(result:String)
    {
        MediaScannerConnection.scanFile(this, arrayOf(result),null)
        {
            path,uri->
            val shareIntent=Intent()
            shareIntent.action=Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM,uri)
            shareIntent.type="Image/png"
            startActivity(Intent.createChooser(shareIntent,"share"))
        }
    }
}

