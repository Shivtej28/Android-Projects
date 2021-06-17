package com.shivtej.androidprojects.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shivtej.androidprojects.R

class QuizAdapter(private val list: List<String>, private val onClickListener: QuizItemClicked):
RecyclerView.Adapter<QuizAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvQuizNumber: TextView = itemView.findViewById(R.id.tv_quiz_no)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.quiz_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizAdapter.ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.tvQuizNumber.text = currentItem
        holder.itemView.setOnClickListener {
            onClickListener.onItemClicked(currentItem)
        }
    }

    override fun getItemCount(): Int = list.size
}

interface QuizItemClicked{
    fun onItemClicked(quiz: String)
}