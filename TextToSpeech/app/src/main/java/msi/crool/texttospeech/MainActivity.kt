package msi.crool.texttospeech

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import msi.crool.texttospeech.databinding.ActivityMainBinding
import java.util.Locale


class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var viewBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding?.root)

        tts= TextToSpeech(this,this)

        viewBinding?.btnField?.setOnClickListener {

        if(viewBinding?.textfield?.text!!.isEmpty())
            {
               Toast.makeText(this,"Pls Entered Text",Toast.LENGTH_LONG).show()
            }
            else
            {
              speakOut(viewBinding?.textfield?.text.toString())
            }
        }
    }

    private fun speakOut(text:String)
    {
        tts?.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }

    override fun onDestroy() {
        super.onDestroy()
        tts?.stop()
        tts?.shutdown()
        viewBinding=null
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS)
        {
            val result=tts!!.setLanguage(Locale.CHINESE)
            if(result == TextToSpeech.LANG_MISSING_DATA || result ==TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Toast.makeText(this,"WRONG",Toast.LENGTH_LONG).show()
            }
        }else
        {
            Toast.makeText(this,"Worked",Toast.LENGTH_LONG).show()
        }
    }
}
