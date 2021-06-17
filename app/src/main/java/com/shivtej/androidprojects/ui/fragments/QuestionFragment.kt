package com.shivtej.androidprojects.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.card.MaterialCardView
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.databinding.FragmentQuestionBinding
import com.shivtej.androidprojects.models.Question
import com.shivtej.androidprojects.viewModels.ProjectViewModel
import de.hdodenhof.circleimageview.CircleImageView

class QuestionFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentQuestionBinding
    private val TAG = "QuestionFragment"
    private val viewModel: ProjectViewModel by activityViewModels()
    private lateinit var questionsList: List<Question>
    private lateinit var question: Question

    private var questionNumber = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val quizname = arguments?.getString("quizname").toString()

       
        questionsList = arrayListOf()

        viewModel.getQuestions(quizname).observe(viewLifecycleOwner, {
            questionsList = it
            showQuestion()
            Log.i(TAG, questionsList[0].toString())
        })



        Log.d(TAG, questionsList.toString())

        binding.cvOption1.setOnClickListener(this)
        binding.cvOption2.setOnClickListener(this)
        binding.cvOption3.setOnClickListener(this)
        binding.cvOption4.setOnClickListener(this)


    }

    private fun showQuestion() {

        question = questionsList[questionNumber]
        binding.tvQuestion.text = question.question
        binding.tvOption1.text = question.option1
        binding.tvOption2.text = question.option2
        binding.tvOption3.text = question.option3
        binding.tvOption4.text = question.option4
        val number = questionNumber+1
        "$number/10".also { binding.questionNumber.text = it }
        disableEnableCVOption(true)
        setCardBackgroundWhite()


    }

    private fun setCardBackgroundWhite() {

        binding.cvOption1.background.setTint(Color.WHITE)
        binding.ivOption1.setImageResource(R.drawable.a_option)
        binding.cvOption2.background.setTint(Color.WHITE)
        binding.ivOption2.setImageResource(R.drawable.b_option)
        binding.cvOption3.background.setTint(Color.WHITE)
        binding.ivOption3.setImageResource(R.drawable.c_option)
        binding.cvOption4.background.setTint(Color.WHITE)
        binding.ivOption4.setImageResource(R.drawable.d_option)

    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.cvOption1 -> {
                checkAnswer(binding.tvOption1, binding.cvOption1, binding.ivOption1)
            }
            R.id.cvOption2 -> {
                checkAnswer(binding.tvOption2, binding.cvOption2, binding.ivOption2)
            }
            R.id.cvOption3 -> {
                checkAnswer(binding.tvOption3, binding.cvOption3, binding.ivOption3)
            }
            R.id.cvOption4 -> {
                checkAnswer(binding.tvOption4, binding.cvOption4, binding.ivOption4)
            }

        }

    }

    private fun checkAnswer(
        tvOption: TextView,
        cvOption: MaterialCardView,
        ivOption: CircleImageView
    ) {

        disableEnableCVOption(false)
        val answer = question.answer
        val selectedAns = tvOption.text.toString()
        Log.d(TAG, "Answer")
        if (answer == selectedAns) {
            cvOption.background.setTint(Color.GREEN)
            ivOption.setImageResource(R.drawable.tick_circle)
            Log.d(TAG, "Correct Ans")
        } else {
            cvOption.background.setTint(Color.RED)
            ivOption.setImageResource(R.drawable.ic_wrong)
            getCorrectAnswer()
        }
        questionNumber++
        val handler = Handler()
        handler.postDelayed({
            showQuestion()
        }, 3000)


    }

    private fun getCorrectAnswer() {
        when (question.answer) {
            binding.tvOption1.text.toString() -> {
                binding.cvOption1.background.setTint(Color.GREEN)
                binding.ivOption1.setImageResource(R.drawable.tick_circle)
            }
            binding.tvOption2.text.toString() -> {
                binding.cvOption2.background.setTint(Color.GREEN)
                binding.ivOption2.setImageResource(R.drawable.tick_circle)
            }
            binding.tvOption3.text.toString() -> {
                binding.cvOption3.background.setTint(Color.GREEN)
                binding.ivOption3.setImageResource(R.drawable.tick_circle)
            }
            else -> {
                binding.cvOption4.background.setTint(Color.GREEN)
                binding.ivOption4.setImageResource(R.drawable.tick_circle)
            }
        }
    }

    private fun disableEnableCVOption(b: Boolean) {
        binding.cvOption1.isEnabled = b
        binding.cvOption2.isEnabled = b
        binding.cvOption3.isEnabled = b
        binding.cvOption4.isEnabled = b
    }
}
