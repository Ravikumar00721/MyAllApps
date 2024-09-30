package com.example.itemtouchhelper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemRecyclerAdpater(private var list:ArrayList<String>,private val context: Context):RecyclerView.Adapter<ItemRecyclerAdpater.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem=list[position]
        holder.itemtext.text=currentItem
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        val item = list.removeAt(fromPosition)
        list.add(toPosition, item)
        notifyItemMoved(fromPosition, toPosition)
    }
    override fun getItemCount(): Int {
        return list.size
    }
    class ViewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        val itemtext:TextView=itemview.findViewById(R.id.text_id)
    }
}