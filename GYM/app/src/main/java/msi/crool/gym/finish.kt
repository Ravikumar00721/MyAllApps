package msi.crool.gym

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FinishActivity : AppCompatActivity() {
    private var btnFinish: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        btnFinish = findViewById(R.id.finish_btn)
        btnFinish?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val dao = (application as WorkOutApp).db.historyDao()
        addDateToDB(dao)
    }

    private fun addDateToDB(historyDao: HistoryDao) {
        val calendar = Calendar.getInstance()
        val datetime = calendar.time

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(datetime)

        lifecycleScope.launch {
            // Create a HistoryEntity instance
            val historyEntity = HistoryEntity(date = date)
            historyDao.insert(historyEntity)
            Log.d("DATE: " ,"Date saved successsfully")
        }
    }

}
