package msi.crool.unitconverter

import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import msi.crool.unitconverter.ui.theme.UnitConverterTheme
import java.time.format.TextStyle
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnitConverterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UnitConverter()
                }
            }
        }
    }
}

@Composable
fun UnitConverter()
{
    var inputValue by remember { mutableStateOf("") }
    var outputValue by remember { mutableStateOf("") }
    var inputUnit by remember { mutableStateOf("Meters") }
    var outputunit by remember { mutableStateOf("Meters") }
    var iexpanded by remember { mutableStateOf(false) }
    var oexpanded by remember { mutableStateOf(false) }
    val conversionFactor=remember { mutableStateOf(1.00) }
    val oConversionFactor=remember { mutableStateOf(1.00) }
    val customTextStyle= androidx.compose.ui.text.TextStyle(
        fontFamily = FontFamily.Default,
        fontSize=32.sp,
        color= Color.Red
    )
    fun convertUnits()
    {
        //?: elvis operator
        val inputValueDouble=inputValue.toDoubleOrNull()?:0.0
        val result=(inputValueDouble*conversionFactor.value*100.0/oConversionFactor.value).roundToInt()/100.0
        outputValue=result.toString()
    }
    Column (
        modifier=Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("UnitConverter",modifier= Modifier.padding(50.dp),style=customTextStyle)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = inputValue, onValueChange = {
            inputValue=it
        },label={ Text(text = "Enter Value")})
        val context= LocalContext.current
        Spacer(modifier= Modifier.height(16.dp))
        Row {
         Box {
           Button(onClick = { iexpanded=true }) {
               Text(inputUnit)
               Icon(Icons.Default.ArrowDropDown, contentDescription = "Arrow Down")
           }
             DropdownMenu(expanded = iexpanded, onDismissRequest = { iexpanded=false }) {
                 DropdownMenuItem(
                     text = { Text(text = "Centimeters") },
                     onClick = {
                         iexpanded=false
                         inputUnit="Centimeters"
                         conversionFactor.value=0.01
                         convertUnits()
                     })
                 DropdownMenuItem(
                     text = { Text(text = "Meters") },
                     onClick = {
                         iexpanded=false
                         inputUnit="Meters"
                         conversionFactor.value=1.0
                         convertUnits() })
                 DropdownMenuItem(
                     text = { Text(text = "Feet") },
                     onClick = {
                         iexpanded=false
                         inputUnit="Feet"
                         conversionFactor.value=0.3048
                         convertUnits()
                     })
                 DropdownMenuItem(
                     text = { Text(text = "MilliMetres") },
                     onClick = {
                         iexpanded=false
                         inputUnit="MilliMeters"
                         conversionFactor.value=0.001
                         convertUnits()
                     })
             }
         }
            Spacer(modifier=Modifier.width(16.dp))
         Box {
             Button(onClick = { oexpanded=true }) {
                 Text(outputunit)
                 Icon(Icons.Default.ArrowDropDown, contentDescription = "Arrow Down")
             }
             DropdownMenu(expanded = oexpanded, onDismissRequest = { oexpanded=false }) {
                 DropdownMenuItem(
                     text = { Text(text = "Centimeters") },
                     onClick = {
                         oexpanded=false
                         outputunit="Centimeters"
                         oConversionFactor.value=0.01
                         convertUnits()
                     })
                 DropdownMenuItem(
                     text = { Text(text = "Meters") },
                     onClick = {
                         oexpanded=false
                         outputunit="Meters"
                         oConversionFactor.value=1.00
                         convertUnits()
                     })
                 DropdownMenuItem(
                     text = { Text(text = "Feet") },
                     onClick = {
                         oexpanded=false
                         outputunit="Feet"
                         oConversionFactor.value=0.3048
                         convertUnits()
                     })
                 DropdownMenuItem(
                     text = { Text(text = "MilliMetres") },
                     onClick = {
                         oexpanded=false
                         outputunit="MilliMeters"
                         oConversionFactor.value=0.001
                         convertUnits()
                     })
             }
         }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Result : $outputValue")
    }
}

//@Preview(showBackground = true)
//@Composable
//fun UnitConverterPreview()
//{
//   UnitConverter()
//}

