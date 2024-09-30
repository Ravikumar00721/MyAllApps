package msi.crool.mediaplayer

import android.annotation.SuppressLint
import android.os.Bundle
import android.media.MediaPlayer
import android.net.Uri
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import msi.crool.mediaplayer.ui.theme.MediaPlayerTheme

class MainActivity : ComponentActivity() {
    private var playing:Button?=null
    private var pause:Button?=null
    private var player:MediaPlayer?=null
    private var isPlaying: Boolean = false
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playing=findViewById(R.id.play)
        pause=findViewById(R.id.pause)
        playing?.setOnClickListener {
         if(!isPlaying)
         {
             val soundURI=Uri.parse("android.resource://msi.crool.mediaplayer/" + R.raw.game)
             player=MediaPlayer.create(applicationContext,soundURI)
             player?.isLooping=false
             player?.start()
             isPlaying=true
         }
        }
        pause?.setOnClickListener {
            if(isPlaying)
            {
                player?.pause()
                isPlaying=false
            }
        }
    }
}

