package msi.crool.ageapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import msi.crool.ageapp.ui.theme.AgeAppTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    setContentView(R.layout.activity_xml)
                    val Btn_Date_Picker: Button = findViewById(R.id.buttonDatePicker)

                    Btn_Date_Picker.setOnClickListener {
                        clickDatePicker()
                    }
                }
            }
        }
    }

    private fun clickDatePicker() {
        val myCalender = Calendar.getInstance()
        val year = myCalender.get(Calendar.YEAR)
        val month = myCalender.get(Calendar.MONTH)
        val date = myCalender.get(Calendar.DATE)

            val dfd=DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    val dateTextView: TextView = findViewById(R.id.textView7)
                    val minTextView: TextView = findViewById(R.id.textView9)
                    val daysTextView:TextView=findViewById(R.id.textView10)
                    val formattedText = "$selectedYear/${selectedMonth + 1}/$selectedDayOfMonth"
                    dateTextView.text = formattedText
                    Toast.makeText(this, "Date Picked", Toast.LENGTH_LONG).show()

                    val sdf = SimpleDateFormat("dd/MM/yy", Locale.ENGLISH)
                    val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                    val theDate = sdf.parse(selectedDate)
                    val selectedInMinutes = theDate?.time?.div(60000) ?: 0

                    val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))
                    val currentDateInMinutes = currentDate?.time?.div(60000) ?: 0

                    val differenceInMinutes = currentDateInMinutes - selectedInMinutes
                    minTextView.text = differenceInMinutes.toString()

                    val differenceInDays = differenceInMinutes / (60 * 24)
                    daysTextView.text = differenceInDays.toString()
                },
                year,
                month,
                date
            )
            dfd.datePicker.maxDate=System.currentTimeMillis()-86400000
            dfd.show()
    }
}
