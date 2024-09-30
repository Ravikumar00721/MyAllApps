package msi.crool.viewbindingr

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import msi.crool.viewbindingr.databinding.ActivityMainBinding
import msi.crool.viewbindingr.ui.theme.ViewBindingRTheme

class MainActivity : ComponentActivity() {
    private var binding:ActivityMainBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val adapter=MainAdapter(TaskList.tasklists)

        binding?.recyclerView?.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding?.recyclerView?.adapter=adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}

