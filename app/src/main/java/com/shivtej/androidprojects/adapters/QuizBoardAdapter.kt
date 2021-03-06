package com.shivtej.androidprojects.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shivtej.androidprojects.R

class QuizBoardAdapter(

    private val list : List<String>,
    private val onClickListener: BoxClicked) : RecyclerView.Adapter<QuizBoardAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quiz_item_no, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.tvQuizNumber.text = currentItem
        holder.itemView.setOnClickListener {
            onClickListener.onBoxClicked(currentItem)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvQuizNumber : TextView = itemView.findViewById(R.id.tvQuizNumber)
    }

}

interface BoxClicked{
    fun onBoxClicked(string: String)
}

