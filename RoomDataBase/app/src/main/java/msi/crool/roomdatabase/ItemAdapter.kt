package msi.crool.roomdatabase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import msi.crool.roomdatabase.databinding.RecycleitemBinding

class ItemAdapter(
    private val items: ArrayList<EmployeEntity>,
    private val updateListener: (Int) -> Unit,  // Listener for update
    private val deleteListener: (Int) -> Unit   // Listener for delete
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(binding: RecycleitemBinding) : RecyclerView.ViewHolder(binding.root) {
        val llMain = binding.main
        val name = binding.textName
        val email = binding.textEmail
        val edit = binding.edit
        val delete = binding.delete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecycleitemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]

        holder.name.text = item.name
        holder.email.text = item.EmailID

        // Alternate row color
        if (position % 2 == 0) {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.teal_200))
        } else {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

        // Set click listeners for edit and delete
        holder.edit.setOnClickListener {
            updateListener.invoke(item.id)
        }

        holder.delete.setOnClickListener {
            deleteListener.invoke(item.id)
        }
    }
}
