package com.shivtej.androidprojects.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shivtej.androidprojects.models.Project

class ProjectAdapter(private val list: List<Project>,private val onClickListener: ItemClicked) :
    RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}

interface ItemClicked{
    fun onItemClicked(string: Project)
}