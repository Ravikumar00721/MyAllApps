package msi.crool.datepicker

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import msi.crool.datepicker.ui.theme.DatePickerTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : ComponentActivity() {
    private var selDate:Button?=null
//    private var DateView:TextView?=null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        selDate=findViewById(R.id.CalenderSelect)
//        DateView=findViewById(R.id.dateview)
//        selDate?.setOnClickListener {
//            clickDatepicker()
//            Toast.makeText(this,"Button Pressed",Toast.LENGTH_LONG).show()
//        }
    }
//    @SuppressLint("SetTextI18n")
//    private fun clickDatepicker()
//    {
//        val myCalender=Calendar.getInstance()
//        val year=myCalender.get(Calendar.YEAR)
//        val month=myCalender.get(Calendar.MONTH)
//        val day=myCalender.get(Calendar.DAY_OF_MONTH)
//        val dp=android.app.DatePickerDialog(
//            this,
//            { _, Syear, Smonth, SdayOfMonth ->
//
//                // Create a Calendar instance with the selected date
//                val selectedDate = Calendar.getInstance().apply {
//                    set(Syear,Smonth, SdayOfMonth)
//                }.time
//                val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.UK)
//
//                // Format the selected date
//                val formattedDate = dateFormat.format(selectedDate)
//
//                // Update the DateView with the formatted date
//                DateView?.text = formattedDate
//            },
//            year,
//            month,
//            day
//        )
//        dp.show()
//    }
}
