package com.shivtej.androidprojects.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.models.Project
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(private val list: List<Project>) :
    SliderViewAdapter<SliderAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.project_ss_image_view)
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val view =
            LayoutInflater.from(parent?.context).inflate(
                R.layout.image_slider_item, parent, false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, position: Int) {
        val currentItem = list[position]
        if (viewHolder != null) {
            Glide.with(viewHolder.itemView).load(currentItem.ss1).into(viewHolder.imageView)
            Glide.with(viewHolder.itemView).load(currentItem.ss2).into(viewHolder.imageView)
            Glide.with(viewHolder.itemView).load(currentItem.ss3).into(viewHolder.imageView)
            Glide.with(viewHolder.itemView).load(currentItem.ss4).into(viewHolder.imageView)
            Glide.with(viewHolder.itemView).load(currentItem.ss5).into(viewHolder.imageView)
        }
    }

}