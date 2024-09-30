package msi.crool.gym

import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import msi.crool.gym.R
import msi.crool.gym.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {
    private var viewBinding:ActivityMainBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding=ActivityMainBinding.inflate(layoutInflater)

        setContentView(viewBinding?.root)
        viewBinding?.kkr?.setOnClickListener {
            val intent=Intent(this,ExcerciseScreen::class.java)
            startActivity(intent)
        }

        viewBinding?.BMI?.setOnClickListener {
            val int=Intent(this,BMIActivity::class.java)
            startActivity(int)
        }

        viewBinding?.Calender?.setOnClickListener {
            val int=Intent(this,calender::class.java)
            startActivity(int)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding=null
    }
}
