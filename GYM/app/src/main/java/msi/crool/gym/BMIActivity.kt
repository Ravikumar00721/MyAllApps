package msi.crool.gym

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isEmpty
import msi.crool.gym.databinding.ActivityBmiactivityBinding
import msi.crool.gym.databinding.ActivityMainBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNIT_VIEW="METRIC_UNIT_VIEW"
        private const val US_UNIT_VIEW="US_UNIT_VIEW"
    }

    private var currentVisiView:String= METRIC_UNIT_VIEW

    private var binding:ActivityBmiactivityBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityBmiactivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        makeVisibleMetricUSView()

        binding?.rGroup?.setOnCheckedChangeListener { _, checkedId:Int ->

             if(checkedId==R.id.w1_btn)
             {
                 makeVisibleMetricView()
             }
            else
             {
                 makeVisibleMetricUSView()
             }
        }

//        binding?.btnBmi?.setOnClickListener {
//            if(validateMatricUnit())
//            {
//                val weightVal:Float=binding?.w1?.text.toString().toFloat()
//                val heightVal:Float=binding?.h1?.text.toString().toFloat()/100
//
//                val bmi=weightVal/(heightVal*heightVal)
//                displayBMIRes(bmi)
//            }
//            else
//            {
//                Toast.makeText(this@BMIActivity,"Please Enter Valid Valuessss",Toast.LENGTH_LONG).show()
//            }
//        }

        binding?.btnBmi?.setOnClickListener {
            calculateUSUnits()
        }

    }

    private fun makeVisibleMetricView()
    {
        currentVisiView= METRIC_UNIT_VIEW
        binding?.w1?.visibility=View.VISIBLE
        binding?.h1?.visibility=View.VISIBLE
        binding?.feet?.visibility=View.GONE
        binding?.inches?.visibility=View.GONE
        binding?.pounds?.visibility=View.GONE

        binding?.w1?.text!!.clear()
        binding?.h1?.text!!.clear()

        binding?.bmiNum?.visibility=View.INVISIBLE
    }

    private fun makeVisibleMetricUSView()
    {
        currentVisiView= US_UNIT_VIEW
        binding?.w1?.visibility=View.GONE
        binding?.h1?.visibility=View.GONE
        binding?.feet?.visibility=View.VISIBLE
        binding?.inches?.visibility=View.VISIBLE
        binding?.pounds?.visibility=View.VISIBLE

        binding?.w1?.text!!.clear()
        binding?.h1?.text!!.clear()

        binding?.bmiNum?.visibility=View.INVISIBLE
    }

    private fun displayBMIRes(bmi: Float) {
        val bmiLabel: String
        val bmiDescription: String

        when {
            bmi <= 15f -> {
                bmiLabel = "Kuposit"
                bmiDescription = "Chicken aur Mutton Khana suru ker do jld"
            }
            bmi in 15f..16f -> {
                bmiLabel = "Underweight"
                bmiDescription = "Chicken aur Mutton Khana suru ker do jld"
            }
            bmi in 16f..25f -> { // Adjusted range to include BMI values between 16 and 25
                bmiLabel = "Normal"
                bmiDescription = "Congratulations you are in good shape"
            }
            bmi in 25f..30f -> {
                bmiLabel = "Overweight"
                bmiDescription = "Consider adopting a healthier lifestyle"
            }
            bmi > 30f -> {
                bmiLabel = "Obese"
                bmiDescription = "It's important to consult with a healthcare provider for advice"
            }
            else -> {
                bmiLabel = "Unknown"
                bmiDescription = "BMI value is out of expected range"
            }
        }

        val bmivalue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()
        binding?.bmiNum?.visibility = View.VISIBLE
        binding?.bmiNum?.text = bmivalue
        binding?.overweight?.text = bmiLabel
        binding?.oops?.text = bmiDescription
    }



    private fun validateMatricUnit():Boolean
    {
        var isValid=true
        if(binding?.w1?.text.toString().isEmpty() == true)
        {
            isValid=false
        }else if(binding?.h1?.text.toString().isEmpty() ==true)
        {
            isValid=false
        }
        return isValid
    }

    private fun calculateUSUnits()
    {
        if(currentVisiView== METRIC_UNIT_VIEW)
        {
            if(validateMatricUnit())
            {
                val weightVal:Float=binding?.w1?.text.toString().toFloat()
                val heightVal:Float=binding?.h1?.text.toString().toFloat()/100

                val bmi=weightVal/(heightVal*heightVal)
                displayBMIRes(bmi)
            }
            else
            {
                Toast.makeText(this@BMIActivity," Valid Values Bro",Toast.LENGTH_LONG).show()
            }
        }
        else
        {
            if(validateUSMatricUnit())
            {
                val UUHVF:String=binding?.f1?.text.toString()
                val UUIV:String=binding?.i1?.text.toString()
                val UUW:Float=binding?.P1?.text.toString().toFloat()

                val heightVal=UUIV.toFloat()+UUHVF.toFloat()*12

                val bmi=703*(UUW/(heightVal*heightVal))

                displayBMIRes(bmi)

            }
            else
            {
                Toast.makeText(this@BMIActivity,"Shi se Likh",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateUSMatricUnit():Boolean
    {
        var isValid=true
        if(binding?.f1?.text.toString().isEmpty() == true)
        {
            isValid=false
        }else if(binding?.i1?.text.toString().isEmpty() ==true)
        {
            isValid=false
        }
        return isValid
    }
}