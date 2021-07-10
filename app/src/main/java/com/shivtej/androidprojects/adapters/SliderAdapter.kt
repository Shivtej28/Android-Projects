package com.shivtej.androidprojects.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.adapters.SliderAdapter.ViewHolder
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(private val list: ArrayList<String>) :
    RecyclerView.Adapter<SliderAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.project_ss_image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_slider_item,parent, false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val currentItem = list[position]
        Log.d("Link", currentItem)
        Glide.with(holder.itemView.context).load(currentItem).into(holder.imageView)
    }

    override fun getItemCount(): Int = list.size


}