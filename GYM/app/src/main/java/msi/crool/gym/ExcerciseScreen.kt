package msi.crool.gym

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import msi.crool.gym.databinding.ActivityExcerciseScreenBinding
import msi.crool.gym.databinding.DialogCustombackConfirmBinding
import java.util.Locale

class ExcerciseScreen : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var viewB: ActivityExcerciseScreenBinding? = null

    private var countDownTimer: CountDownTimer? = null
    private var restTimer: Int = 0

    private var excerciseTimer: CountDownTimer? = null
    private var excerProgress: Int = 0

    private var excerciselist: ArrayList<ExerciseModel>? = null
    private var currentPosition = -1

    private var tts: TextToSpeech? = null
    private var isTtsInitialized = false
    private var startmusic: MediaPlayer? = null

    private var exerciseAdapter: ExerciseAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewB = ActivityExcerciseScreenBinding.inflate(layoutInflater)
        setContentView(viewB?.root)

        excerciselist = Constants.defaultExcerciseList()

        viewB?.progressbar?.visibility = View.VISIBLE

        tts = TextToSpeech(this, this)

        setRestView()
        setupExerciseRecycleView()
    }

    private fun setupExerciseRecycleView() {
        viewB?.rView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseAdapter(excerciselist!!)
        viewB?.rView?.adapter = exerciseAdapter
    }

    private fun setRestView() {
        try {
            val soundURI = Uri.parse("android.resource://msi.crool.gym/" + R.raw.start)
            startmusic = MediaPlayer.create(applicationContext, soundURI)
            startmusic?.isLooping = false
            startmusic?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        viewB?.F1?.visibility = View.VISIBLE
        viewB?.getExe?.visibility = View.INVISIBLE
        viewB?.getReady?.text = "GET READY FOR"
        viewB?.img?.visibility = View.INVISIBLE
        viewB?.belowTxt?.visibility = View.VISIBLE
        viewB?.belowTxt2?.visibility = View.VISIBLE
        if (countDownTimer != null) {
            countDownTimer?.cancel()
            restTimer = 0
        }
        viewB?.belowTxt?.text = "UPCOMING EXERCISE :"
        startTimer()
        viewB?.belowTxt2?.text = excerciselist!![currentPosition + 1].getName()
        if (isTtsInitialized) {
            speakOut("get ready for upcoming exercise " + viewB?.belowTxt2?.text.toString())
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        customDialogForBackButton()
    }

    private fun customDialogForBackButton() {
        val customD = Dialog(this)
        val dialogbinding = DialogCustombackConfirmBinding.inflate(layoutInflater)
        customD.setContentView(dialogbinding.root)
        customD.setCanceledOnTouchOutside(false)

        dialogbinding.Yes.setOnClickListener {
            this@ExcerciseScreen.finish()
            customD.dismiss()
        }
        dialogbinding.No.setOnClickListener {
            customD.dismiss()
        }
        customD.show()
    }

    private fun startTimer() {
        restTimer = 0
        viewB?.progressbar?.progress = restTimer
        countDownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restTimer++
                viewB?.progressbar?.progress = 10 - restTimer
                viewB?.txt?.text = (10 - restTimer).toString()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onFinish() {
                currentPosition++
                excerciselist!![currentPosition].setSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setUpExerciseView()
            }
        }.start()
    }

    private fun setUpExerciseView() {
        viewB?.F1?.visibility = View.INVISIBLE
        viewB?.getExe?.visibility = View.VISIBLE
        viewB?.getReady?.text = "EXERCISE NAME"
        viewB?.img?.visibility = View.VISIBLE
        viewB?.belowTxt?.visibility = View.INVISIBLE
        viewB?.belowTxt2?.visibility = View.INVISIBLE
        if (excerciseTimer != null) {
            excerciseTimer?.cancel()
            excerProgress = 0
        }

        viewB?.img?.setImageResource(excerciselist!![currentPosition].getImage())
        viewB?.getReady?.text = excerciselist!![currentPosition].getName()
        if (isTtsInitialized) {
            speakOut(excerciselist!![currentPosition].getName())
        }
        setProgressBar()
    }

    private fun setProgressBar() {
        excerProgress = 0
        viewB?.progressbar2?.progress = excerProgress
        excerciseTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                excerProgress++
                viewB?.progressbar2?.progress = 30 - excerProgress
                viewB?.txt2?.text = (30 - excerProgress).toString()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onFinish() {
                excerciselist!![currentPosition].setSelected(false)
                excerciselist!![currentPosition].setCompleted(true)
                exerciseAdapter!!.notifyDataSetChanged()

                if (currentPosition < excerciselist!!.size - 1) {
                    setRestView()
                } else {
                    val intent = Intent(this@ExcerciseScreen,FinishActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (excerciseTimer != null) {
            excerciseTimer?.cancel()
            excerProgress = 0
        }
        if (countDownTimer != null) {
            countDownTimer?.cancel()
            restTimer = 0
        }
        tts?.shutdown()
        viewB = null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "TTS language is not supported", Toast.LENGTH_LONG).show()
            } else {
                isTtsInitialized = true
                // Speak out the first exercise when TTS is initialized
                if (currentPosition == -1) {
                    speakOut("get ready for upcoming exercise " + excerciselist!![currentPosition + 1].getName())
                }
            }
        } else {
            Toast.makeText(this, "TTS initialization failed", Toast.LENGTH_LONG).show()
        }
    }

    private fun speakOut(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}
