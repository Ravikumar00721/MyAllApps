package msi.crool.saptap.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.provider.Settings
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.annhienktuit.circularrotationimageview.RotateAnimation
import msi.crool.saptap.Model.Speeches
import msi.crool.saptap.R
import msi.crool.saptap.Services.SpeechService
import msi.crool.saptap.Services.SpeechBuilder
import msi.crool.saptap.databinding.ActivitySpeechToTextBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SpeechToText : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivitySpeechToTextBinding? = null
    private val contextBuilder = StringBuilder() // Holds conversation context
    private val speechRecognizer: SpeechRecognizer by lazy { SpeechRecognizer.createSpeechRecognizer(this) }
    private var tts: TextToSpeech? = null
    private lateinit var animation: RotateAnimation
    private var handlerAnimation = Handler()

    // Permissions request
    private val allowPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpeechToTextBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Initialize the text-to-speech and animation
        tts = TextToSpeech(this, this)
        animation = RotateAnimation(this, findViewById(R.id.speaker_image))
        animation.setDuration(20000)

        if (!isInternetAvailable()) {
            showInternetSettingsDialog()
            return
        }

        setupActionBar()

        // Initialize the speech recognizer listener
        setupSpeechRecognizer()

        // Handle touch events to trigger speech-to-text
        binding?.OurImage?.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_UP -> {
                    stopListeningAndSpeaking()
                    stopPulse()
                    true
                }
                MotionEvent.ACTION_DOWN -> {
                    getPermissionRecord {
                        startListen()
                        startPulse()
                    }
                    true
                }
                else -> false
            }
        }

        // Clear text button
        binding?.btnClearText?.setOnClickListener {
            contextBuilder.clear()
            stopListeningAndSpeaking()
            stopRotationAnimation()
        }

        // History button
        binding?.history?.setOnClickListener {
            stopListeningAndSpeaking()
        }
    }

    // Setting up the SpeechRecognizer listener
    private fun setupSpeechRecognizer() {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}

            override fun onError(error: Int) {
                Log.e("SpeechToText", "Recognition error: $error")
                stopRotationAnimation()
            }

            override fun onResults(results: Bundle?) {
                val recognizedText = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull() ?: "No speech recognized."

                contextBuilder.append("USER: $recognizedText\n")
                postSpeechAndHandleResponse(recognizedText)
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    private fun startListen() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now")
        }
        speechRecognizer.startListening(intent)
    }

    private fun getPermissionRecord(call: () -> Unit) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            call()
        } else {
            allowPermission.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    private fun showInternetSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("No Internet Connection")
            .setMessage("Please enable internet to use this feature.")
            .setPositiveButton("Settings") { _, _ ->
                startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                finish()
            }
            .setNegativeButton("Exit") { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }

    @SuppressLint("MissingPermission")
    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun startPulse() {
        handlerAnimation.post(runnable)
    }

    private fun stopPulse() {
        handlerAnimation.removeCallbacks(runnable)
    }

    private val runnable = object : Runnable {
        override fun run() {
            binding?.OurImage?.animate()
                ?.scaleX(1.5f)
                ?.scaleY(1.5f)
                ?.alpha(0f)
                ?.setDuration(1000)
                ?.withEndAction {
                    binding?.OurImage?.scaleX = 1f
                    binding?.OurImage?.scaleY = 1f
                    binding?.OurImage?.alpha = 1f
                }
            handlerAnimation.postDelayed(this, 1500)
        }
    }

    private fun startRotationAnimation() {
        animation.startAnimation()
    }

    private fun stopRotationAnimation() {
        animation.pause()
    }

    private fun postSpeechAndHandleResponse(recognizedText: String) {
        val service = SpeechBuilder.speechBuilder(SpeechService::class.java)
        val speechData = mapOf("input" to recognizedText, "context" to contextBuilder.toString())

        val call = service.addDestination(speechData)
        call.enqueue(object : Callback<Speeches> {
            override fun onResponse(call: Call<Speeches>, response: Response<Speeches>) {
                if (response.isSuccessful) {
                    val serverResponse = response.body()?.response ?: "No response from server"
                    Log.d("SpeechToText", "Server response: $serverResponse")
                    contextBuilder.append("AI: $serverResponse\n")
                    speakOut(serverResponse)
                } else {
                    Log.e("SpeechToText", "Server error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Speeches>, t: Throwable) {
                Log.e("SpeechToText", "Network failure", t)
            }
        })
    }

    private fun speakOut(text: String) {
        if (tts?.language != Locale.getDefault()) {
            tts?.language = Locale.getDefault()
        }
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        startRotationAnimation()
    }

    private fun stopListeningAndSpeaking() {
        speechRecognizer.stopListening()
        tts?.stop()
        stopRotationAnimation()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.getDefault()
        } else {
            Log.e("SpeechToText", "TTS Initialization failed")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts?.stop()
        tts?.shutdown()
        speechRecognizer.destroy()
        stopPulse()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.toolbarActivity?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
