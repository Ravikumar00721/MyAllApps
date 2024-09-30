package msi.crool.gym

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import msi.crool.gym.databinding.ActivityCalenderBinding
import msi.crool.gym.databinding.ActivityMainBinding

class calender : AppCompatActivity() {
    private var binding:ActivityCalenderBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCalenderBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val dao=(application as WorkOutApp).db.historyDao()
        getAllCompleteDates(dao)
    }
    private fun getAllCompleteDates(historyDao: HistoryDao) {
        lifecycleScope.launch {
            historyDao.fetchAllDates().collect { allCompleteDatesList ->
                Log.d("calender", "Fetched dates count: ${allCompleteDatesList.size}")
                if (allCompleteDatesList.isNotEmpty()) {
                    binding?.recycleID?.visibility = View.VISIBLE
                    binding?.rUn?.visibility = View.GONE
                    binding?.recycleID?.layoutManager = LinearLayoutManager(this@calender)

                    val dates = ArrayList<String>()
                    for (date in allCompleteDatesList) {
                        dates.add(date.date)
                    }

                    Log.d("calender", "Dates for adapter: $dates")

                    val historyAdapter = HistoryAdapter(dates)
                    binding?.recycleID?.adapter = historyAdapter
                } else {
                    binding?.recycleID?.visibility = View.GONE
                    binding?.rUn?.visibility = View.VISIBLE
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}