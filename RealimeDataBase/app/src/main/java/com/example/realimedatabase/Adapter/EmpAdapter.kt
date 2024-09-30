package com.example.realimedatabase.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.realimedatabase.Model.EmpModel
import com.example.realimedatabase.R

class EmpAdapter(private val empList: ArrayList<EmpModel>) : RecyclerView.Adapter<EmpAdapter.ViewHolder>() {
    lateinit var mListner:onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setItemOnClickListner(clickListener: onItemClickListener)
    {
        mListner=clickListener
    }

    // ViewHolder class to hold the views for each item
    class ViewHolder(itemView: View,clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        // Initialize the TextView for employee name
        val empName: TextView = itemView.findViewById(R.id.item)
        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view,mListner)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val emp = empList[position]
        holder.empName.text = emp.name
    }

    override fun getItemCount(): Int {
        return empList.size
    }
}
