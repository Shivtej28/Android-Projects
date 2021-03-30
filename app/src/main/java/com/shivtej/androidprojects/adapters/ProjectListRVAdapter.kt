package com.shivtej.androidprojects.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.models.Project

class ProjectListRVAdapter(private var projectList : List<Project>, private val onCLickedProject : ProjectItemClicked) : RecyclerView.Adapter<ProjectListRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = projectList[position]
        holder.tvTitle.text = currentItem.title
        holder.itemView.setOnClickListener {
            onCLickedProject.onProjectClicked(currentItem)
        }
        Glide.with(holder.itemView).load(currentItem.image).into(holder.ivProjectImage)


    }

    override fun getItemCount(): Int = projectList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvProjectTitle)
        val ivProjectImage : ImageView = itemView.findViewById(R.id.ivProjectImage)
    }
}

interface ProjectItemClicked{
    fun onProjectClicked(project: Project)
}