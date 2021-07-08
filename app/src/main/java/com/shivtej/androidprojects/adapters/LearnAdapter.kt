package com.shivtej.androidprojects.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.models.LearnBlog
import com.shivtej.androidprojects.ui.fragments.LearnFragment

class LearnAdapter(private val list: List<LearnBlog>, private val onClicked: OnClicked) :
    RecyclerView.Adapter<LearnAdapter.ViewHolder>()  {

        inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
            val tvTitle : TextView = itemView.findViewById(R.id.title)
            val tvContent : TextView = itemView.findViewById(R.id.some_content)
            val popUpMenu : ImageButton = itemView.findViewById(R.id.pop_up_menu)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.learn_list_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.tvTitle.text = currentItem.title
        holder.tvContent.text = currentItem.description

        holder.itemView.setOnClickListener {
            onClicked.onLearnBlogClicked(currentItem)
        }

        holder.popUpMenu.setOnClickListener {
            val popUp = PopupMenu(holder.itemView.context, holder.popUpMenu)
            popUp.inflate(R.menu.learn_menu)
            popUp.show()
        }



    }

    override fun getItemCount(): Int = list.size
}

interface OnClicked{
    fun onLearnBlogClicked(currentItem : LearnBlog)

    fun onMenuMarkAsTodoClicked(currentItem: LearnBlog)

    fun orMenuMarkAsDoneClicked(currentItem: LearnBlog)
}