package msi.crool.gym

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import msi.crool.gym.databinding.ActivityCalenderBinding
import msi.crool.gym.databinding.ItemHistoryRowBinding

class HistoryAdapter(private val items:ArrayList<String> ): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(binding:ItemHistoryRowBinding):RecyclerView.ViewHolder(binding.root)
    {
        val LLHmain=binding.mainID
        val numID=binding.numberID
        val dateID=binding.DateID

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(ItemHistoryRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date: String = items[position]
        holder.numID.text = (position + 1).toString()
        holder.dateID.text = date

        // Alternate background colors
        val backgroundColor = if (position % 2 == 0) {
            Color.parseColor("#EBEBEB") // Light Gray
        } else {
            Color.parseColor("#FFFFFF") // White
        }

        holder.LLHmain.setBackgroundColor(backgroundColor)
    }

}