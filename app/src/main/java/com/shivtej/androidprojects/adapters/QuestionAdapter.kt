package com.shivtej.androidprojects.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.databinding.ActivityMainBinding.inflate
import com.shivtej.androidprojects.models.Question
import java.util.*

class QuestionAdapter(private val list: List<Question>): PagerAdapter() {

//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val question: TextView = itemView.findViewById(R.id.questions_text_view)
//        val option1: TextView = itemView.findViewById(R.id.tvOption1)
//        val option2: TextView = itemView.findViewById(R.id.tvOption2)
//        val option3: TextView = itemView.findViewById(R.id.tvOption3)
//        val option4: TextView = itemView.findViewById(R.id.tvOption4)
//
//        fun setData(que: Question){
//            question.text = que.question
//            option1.text = que.option1
//            option2.text = que.option2
//            option3.text = que.option3
//            option4.text = que.option4
//        }
//    }

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context)
            .inflate(R.layout.question_container, container, false)

        val questionTv: TextView = view.findViewById(R.id.questions_text_view)
        val currentItem = list[position]
        questionTv.text = currentItem.question
        Objects.requireNonNull(container).addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}