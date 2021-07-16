package com.shivtej.androidprojects.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.models.Project
import java.util.*

class ProjectListAdapter(private val list: List<Project>, private val onClickListener: IItemClicked) :RecyclerView.Adapter<ProjectListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvProjectName: TextView = itemView.findViewById(R.id.tvProjectName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.project_rv_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.tvProjectName.text = currentItem.title
        holder.itemView.setOnClickListener {
            onClickListener.onItemClicked(currentItem)
        }
    }

    override fun getItemCount(): Int = list.size
}

interface IItemClicked{
    fun onItemClicked(project: Project)

}