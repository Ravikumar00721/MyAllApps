package msi.crool.pulseanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import msi.crool.pulseanimation.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {
    private var binding: ActivityMainBinding? = null
    private var handlerAnimation = android.os.Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Start the pulse animation as soon as the activity is created
        startPulse()
    }

    private fun startPulse() {
        handlerAnimation.post(runnable)
    }

    private fun stopPulse() {
        handlerAnimation.removeCallbacks(runnable)
    }

    private val runnable = object : Runnable {
        override fun run() {
            binding?.imgAnimation1?.animate()?.scaleX(4f)?.scaleY(4f)?.alpha(0f)?.setDuration(1000)
                ?.withEndAction {
                    binding?.imgAnimation1?.scaleX = 1f
                    binding?.imgAnimation1?.scaleY = 1f
                    binding?.imgAnimation1?.alpha = 1f
                }

            binding?.imgAnimation2?.animate()?.scaleX(4f)?.scaleY(4f)?.alpha(0f)?.setDuration(700)
                ?.withEndAction {
                    binding?.imgAnimation2?.scaleX = 1f
                    binding?.imgAnimation2?.scaleY = 1f
                    binding?.imgAnimation2?.alpha = 1f
                }

            // Repeat the animation every 1.5 seconds
            handlerAnimation.postDelayed(this, 1500)
        }
    }
}
