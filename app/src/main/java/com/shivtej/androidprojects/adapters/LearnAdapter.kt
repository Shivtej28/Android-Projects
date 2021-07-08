package com.shivtej.androidprojects.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.models.LearnBlog

class LearnAdapter(private val list: List<LearnBlog>, private val onClicked: OnClicked) :
    RecyclerView.Adapter<LearnAdapter.ViewHolder>() {

    private var like: Boolean = true

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.title)
        val tvContent: TextView = itemView.findViewById(R.id.some_content)
        val bookmark: LottieAnimationView = itemView.findViewById(R.id.bookmark_post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.learn_list_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.tvTitle.text = currentItem.title
        holder.tvContent.text = currentItem.description

        holder.itemView.setOnClickListener {
            onClicked.onLearnBlogClicked(currentItem)
        }

        holder.bookmark.setOnClickListener {

            like = if (like) {
                holder.bookmark.setMinAndMaxProgress(0.0f, 0.5f)
                holder.bookmark.playAnimation()
                false
            } else {
                holder.bookmark.setMinAndMaxProgress(0.5f, 1.0f)
                holder.bookmark.playAnimation()
                true
            }
        }
    }

    override fun getItemCount(): Int = list.size
}

interface OnClicked {
    fun onLearnBlogClicked(currentItem: LearnBlog)
}