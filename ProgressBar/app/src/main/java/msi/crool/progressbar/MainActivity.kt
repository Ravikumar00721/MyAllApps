package msi.crool.progressbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import msi.crool.progressbar.databinding.ActivityMainBinding


class MainActivity : ComponentActivity() {
    private var binding:ActivityMainBinding?=null
    private var progressValue: Int = 50
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        binding?.btn1?.setOnClickListener {
            if (progressValue<100)
            {
                progressValue+=10
                binding?.progressbar?.progress=progressValue
            }
        }
        binding?.btn2?.setOnClickListener {
            if (progressValue>0)
            {
                progressValue-=10
                binding?.progressbar?.progress=progressValue
            }
        }
    }
}

