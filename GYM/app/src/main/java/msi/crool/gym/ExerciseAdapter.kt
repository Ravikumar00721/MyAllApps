package msi.crool.gym

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import msi.crool.gym.databinding.ItemExerStatusBinding

class ExerciseAdapter(
    private val items: ArrayList<ExerciseModel>
) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemExerStatusBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvItem = binding.tvItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemExerStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ExerciseModel = items[position]
        holder.tvItem.text = model.getID().toString()

        when {
            model.isSelected() -> {
                holder.tvItem.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.item_thin)
                holder.tvItem.setTextColor(Color.parseColor("#212121"))
            }
            model.isCompleted() -> {
                holder.tvItem.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.item_acces_background)
                holder.tvItem.setTextColor(Color.parseColor("#212121"))
            }
            else -> {
                holder.tvItem.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.color_gray)
                holder.tvItem.setTextColor(Color.parseColor("#212121"))
            }
        }
    }
}
