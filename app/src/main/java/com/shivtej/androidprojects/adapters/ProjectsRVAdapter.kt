package com.shivtej.androidprojects.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.models.ListGradient

class ProjectsRVAdapter(private var list: List<ListGradient>, private val onClickListener: ItemClicked) : RecyclerView.Adapter<ProjectsRVAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.project_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem  = list[position]
        holder.textView.text = currentItem.item
        holder.linearlayout.setBackgroundResource(currentItem.gradientDrawable)
        holder.itemView.setOnClickListener {
            onClickListener.onItemClicked(list[position])
        }

    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val textView : TextView = itemView.findViewById(R.id.textView)
        val linearlayout : LinearLayout = itemView.findViewById(R.id.linearlayout)
    }
}

interface ItemClicked{
    fun onItemClicked(string: ListGradient)
}