package com.shivtej.androidprojects.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.card.MaterialCardView
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.databinding.ActivityMainBinding.inflate
import com.shivtej.androidprojects.models.Question
import com.shivtej.androidprojects.ui.fragments.QuestionFragment
import java.util.*

class QuestionAdapter(private val list: List<Question>, private val onClicked: OnOptionClicked, val questionFragment: QuestionFragment) : PagerAdapter() {


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
        val tvOption1: TextView = view.findViewById(R.id.tvOption1)
        val tvOption2: TextView = view.findViewById(R.id.tvOption2)
        val tvOption3: TextView = view.findViewById(R.id.tvOption3)
        val tvOption4: TextView = view.findViewById(R.id.tvOption4)
        val cvOption1 : MaterialCardView = view.findViewById(R.id.cvOption1)
        val cvOption2 : MaterialCardView = view.findViewById(R.id.cvOption2)
        val cvOption3 : MaterialCardView = view.findViewById(R.id.cvOption3)
        val cvOption4 : MaterialCardView = view.findViewById(R.id.cvOption4)


        val currentItem = list[position]
        Log.i("posi", position.toString())

        questionTv.text = currentItem.question
        tvOption1.text = currentItem.option1
        tvOption2.text = currentItem.option2
        tvOption3.text = currentItem.option3
        tvOption4.text = currentItem.option4

        cvOption1.setOnClickListener {
            onClicked.onClicked(view, currentItem, 1, position)
        }
        cvOption2.setOnClickListener {
            onClicked.onClicked(view, currentItem, 2, position)
        }
        cvOption3.setOnClickListener {
            onClicked.onClicked(view, currentItem, 3, position)
        }
        cvOption4.setOnClickListener {
            onClicked.onClicked(view, currentItem, 4, position)
        }


        Objects.requireNonNull(container).addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
        questionFragment.changeNumbers(position)
    }
}

interface OnOptionClicked{
    fun onClicked(view : View, currentItem : Question, num : Int, position: Int)
}