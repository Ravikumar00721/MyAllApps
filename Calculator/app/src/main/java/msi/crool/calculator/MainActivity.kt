package msi.crool.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
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
import msi.crool.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    private var tvinput:TextView?=null
    var lastnumeric:Boolean=false
    var lastDecimal:Boolean=false
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvinput=findViewById(R.id.tvInput)
            }
    fun onDigit(view:View)
    {
        tvinput?.append((view as Button).text)
        lastnumeric=true
        lastDecimal=false
//        Toast.makeText(this,"Button Clicked",Toast.LENGTH_LONG).show()
    }
    fun onClear(view: View)
    {
        tvinput?.text=""
    }
    fun onDecimal(view: View)
    {
       if(lastnumeric && !lastDecimal)
       {
           tvinput?.append(".")
           lastnumeric=false
           lastDecimal=true
       }
    }
    private fun removeZero(result:String):String{
        var value=result
        if(result.contains(".0"))
        {
            value=result.substring(0,result.length-2)
        }
        return value
    }
    @SuppressLint("SetTextI18n")
    fun onEqual(view :View)
    {
        if(lastnumeric)
        {
            var tvValue=tvinput?.text.toString()
            var prefix=""
            try {
                if(tvValue.startsWith("-"))
                {
                    prefix="-"
                    tvValue=tvValue.substring(1)
                }
                if(tvValue.contains("-"))
                {
                    val splitValue=tvValue.split("-")
                    var one=splitValue[0]
                    var two=splitValue[1]
                    if(prefix.isNotEmpty())
                    {
                        one=prefix+one
                    }
                    tvinput?.text=removeZero((one.toDouble()-two.toDouble()).toString())
                }else if(tvValue.contains("+"))
                {
                    val splitValue=tvValue.split("+")
                    var one=splitValue[0]
                    var two=splitValue[1]
                    if(prefix.isNotEmpty())
                    {
                        one=prefix+one
                    }
                    tvinput?.text=removeZero((one.toDouble()+two.toDouble()).toString())
                }else if(tvValue.contains("*"))
                {
                    val splitValue=tvValue.split("*")
                    var one=splitValue[0]
                    var two=splitValue[1]
                    if(prefix.isNotEmpty())
                    {
                        one=prefix+one
                    }
                    tvinput?.text=removeZero((one.toDouble()*two.toDouble()).toString())
                }else if(tvValue.contains("/"))
                {
                    val splitValue=tvValue.split("/")
                    var one=splitValue[0]
                    var two=splitValue[1]
                    if(prefix.isNotEmpty())
                    {
                        one=prefix+one
                    }
                    tvinput?.text=removeZero((one.toDouble()/two.toDouble()).toString())
                }
            }catch (e:ArithmeticException)
            {
                e.printStackTrace()
            }
        }
    }

    fun onOperator(view: View)
    {
        tvinput?.text?.let {
            if(lastnumeric && !isOperatorAdded(it.toString()))
            {
                tvinput?.append((view as Button).text)
                lastDecimal=false
                lastnumeric=false
            }
        }
    }
    private fun isOperatorAdded(value:String):Boolean
    {
        return if(value.startsWith("-"))
        {
            false
        }else
        {
            value.contains("/") || value.contains("+")||value.contains("-")||value.contains("*")
        }
    }
}



