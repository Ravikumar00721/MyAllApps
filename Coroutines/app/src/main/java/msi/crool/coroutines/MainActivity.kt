package msi.crool.coroutines

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import msi.crool.coroutines.ui.theme.CoroutinesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buttonc)
        val btn_1:Button=findViewById(R.id.B_1)
        btn_1.setOnClickListener{
            lifecycleScope.launch {
                execute("Task Completed Suceesfully")
            }
        }
    }
    private suspend fun execute(result:String)
    {
        withContext(Dispatchers.IO)
        {
            for(i in 1..100000)
            {
                Log.d("FOR LOOP", "DELAY")
            }
            runOnUiThread {
                Toast.makeText(this@MainActivity,result,Toast.LENGTH_LONG).show()
            }
        }
    }
}

