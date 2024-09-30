package msi.crool.removeactionbar

import android.content.Intent
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
import msi.crool.removeactionbar.databinding.ActivityMainBinding
import msi.crool.removeactionbar.ui.theme.RemoveActionBarTheme

class MainActivity : ComponentActivity() {
    private var binding:ActivityMainBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        binding?.btn1?.setOnClickListener {
            val intent=Intent(this,ToolBarAct::class.java)
            startActivity(intent)
        }

    }
}

