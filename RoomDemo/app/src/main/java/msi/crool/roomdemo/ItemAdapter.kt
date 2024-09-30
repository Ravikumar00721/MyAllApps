package msi.crool.roomdemo

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import msi.crool.roomdemo.databinding.ItemRowBinding

class ItemAdapter(
    private val items: ArrayList<dataClass>,
    private val updateListener: (id: Int) -> Unit,
    private val deleteListener: (id: Int) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val main = binding.main
        val name = binding.nameTextView
        val email = binding.emailTextView
        val edit = binding.editImageView
        val delete = binding.deleteImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context=holder.itemView.context
        val item = items[position]
        holder.name.text = item.name
        holder.email.text = item.email

        // Determine the background color based on position
        val backgroundColor = if (position % 2 == 0) {
            ContextCompat.getColor(context, R.color.c_1)
        } else {
            ContextCompat.getColor(context, R.color.c_2)
        }

        // Log the position and color
        Log.d("ItemAdapter", "Position: $position, Color: $backgroundColor")

        // Set the background color of the main view
        holder.main.setBackgroundColor(backgroundColor)

        // Set listeners for edit and delete actions
        holder.edit.setOnClickListener {
             updateListener(item.id)  // Uncomment and implement this if needed
        }
        holder.delete.setOnClickListener {
             deleteListener(item.id)  // Uncomment and implement this if needed
        }
    }


}
