package msi.crool.demotimers

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ComponentActivity
import msi.crool.demotimers.ui.theme.DemoTimersTheme

@SuppressLint("RestrictedApi")
class MainActivity : ComponentActivity() {
    private var txt:TextView?=null
    private var start_btn:Button?=null
    private var paise_btn:Button?=null
    private var stop_btn:Button?=null

    private var countDownTimer:CountDownTimer?=null
    private var timeDuration:Long=60000
    private var pauseOffset:Long=0

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txt=findViewById(R.id.textview_id)
        start_btn=findViewById(R.id.btn_1)
        paise_btn=findViewById(R.id.btn_2)
        stop_btn=findViewById(R.id.btn_3)

        txt?.text="${(timeDuration/1000).toString()}"

        start_btn?.setOnClickListener{
            startTimer(pauseOffset)
        }
        paise_btn?.setOnClickListener {
               pauseTimer()
        }
        stop_btn?.setOnClickListener {
               resetTimer()
        }
    }
    private fun startTimer(pauseOffset:Long)
    {
        countDownTimer=object :CountDownTimer(timeDuration-pauseOffset,1000)
        {
            override fun onTick(miilisUntilF:Long)
            {
                this@MainActivity.pauseOffset =timeDuration-miilisUntilF
                txt?.text = "${(miilisUntilF/ 1000).toString()}"
            }

            override fun onFinish() {
                Toast.makeText(this@MainActivity,"Timer is Finished",Toast.LENGTH_LONG).show()
            }
        }.start()
    }
    private fun pauseTimer()
    {
        if(countDownTimer!=null)
        {
            countDownTimer!!.cancel()
        }
    }
    private fun resetTimer()
    {
        if(countDownTimer!=null)
        {
            countDownTimer!!.cancel()
            txt?.text="${(timeDuration/1000).toString()}"
            countDownTimer=null
            pauseOffset=0
        }
    }
}

