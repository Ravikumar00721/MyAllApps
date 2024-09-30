package msi.crool.radiobutton

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
import msi.crool.radiobutton.databinding.ActivityMainBinding
import msi.crool.radiobutton.ui.theme.RadioButtonTheme

class MainActivity : ComponentActivity() {
    private var binding:ActivityMainBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.One?.setOnClickListener {
            binding?.One?.isChecked=true
            binding?.two?.isChecked=false
        }
        binding?.two?.setOnClickListener {
            binding?.One?.isChecked=false
            binding?.two?.isChecked=true
        }

    }
}

