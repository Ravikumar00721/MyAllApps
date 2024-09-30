package msi.crool.viewbindingr

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import msi.crool.viewbindingr.databinding.RecycleviewBinding

class MainAdapter(val tasklist:List<Task>):RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    inner class MainViewHolder(val itemBinding:RecycleviewBinding)
        :RecyclerView.ViewHolder(itemBinding.root)
        {
            fun bindItem(task:Task)
            {
                itemBinding.a.text=task.title
                itemBinding.b.text=task.timestamp
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(RecycleviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return tasklist.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val task=tasklist[position]
        holder.bindItem(task)
    }
}