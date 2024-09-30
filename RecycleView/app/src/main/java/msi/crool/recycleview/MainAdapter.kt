package msi.crool.recycleview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import msi.crool.recycleview.databinding.RecycleviewitemBinding

class MainAdapter(private val taskList: MutableList<Task>) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    inner class MainViewHolder(val itembinding: RecycleviewitemBinding) : RecyclerView.ViewHolder(itembinding.root) {
        fun bindItem(task: Task) {
            itembinding.title.text = task.title
            itembinding.time.text = task.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(RecycleviewitemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val task = taskList[position]
        holder.bindItem(task)
    }

    fun deleteItem(position: Int): Task {
        val removedTask = taskList.removeAt(position)
        notifyItemRemoved(position)
        return removedTask
    }

    fun editItem(position: Int, newTitle: String, newTime: String) {
        val editedTask = taskList[position].copy(title = newTitle, time = newTime)
        taskList[position] = editedTask
        notifyItemChanged(position)
    }
}
