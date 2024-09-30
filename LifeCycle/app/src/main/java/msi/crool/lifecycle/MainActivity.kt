package msi.crool.lifecycle

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import msi.crool.lifecycle.ui.theme.LifeCycleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LifeCycleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                     lifecycle.addObserver(Obeserver())
                    Log.d("MAIN","Activity On Create")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("MAIN","Activity On RESUME")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MAIN","Activity On PAUSE")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MAIN","Activity On STOP")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MAIN","Activity On DESTROY")
    }
}

