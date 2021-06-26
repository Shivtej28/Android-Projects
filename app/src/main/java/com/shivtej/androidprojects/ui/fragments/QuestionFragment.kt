package com.shivtej.androidprojects.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.card.MaterialCardView
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.databinding.FragmentQuestionBinding
import com.shivtej.androidprojects.ui.MainActivity
import com.shivtej.androidprojects.models.Question
import com.shivtej.androidprojects.viewModels.ProjectViewModel
import de.hdodenhof.circleimageview.CircleImageView

class QuestionFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentQuestionBinding
    private lateinit var activity1: MainActivity
    private val TAG = "QuestionFragment"
    private val viewModel: ProjectViewModel by activityViewModels()
    private lateinit var questionsList: List<Question>
    private lateinit var question: Question

    private var questionNumber = 0
    private var score = 0
    private var inCorrect = 0
    private var notAttempted = 0

    lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionBinding.inflate(inflater, container, false)
        activity1 = activity as MainActivity
        activity1.hideView()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            showQuitDialog()
        }

        callback.isEnabled = true

    }

    private fun showQuitDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("End Quiz?")
        builder.setPositiveButton("Yes") { _, _ ->
            showScoreDialog()

        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.cancel()
        }

        val alertDialog = builder.create()

        alertDialog.show()
    }

    private fun showScoreDialog() {
        val builder = Dialog(
            requireContext(),
            R.style.Base_ThemeOverlay_MaterialComponents_Dialog_Alert
        )
        builder.setContentView(R.layout.quiz_complete_dialog_box)

        builder.show()
        builder.setCanceledOnTouchOutside(false)

        val tvOk = builder.findViewById<TextView>(R.id.tvOk)
        val tvTotalQuestion = builder.findViewById<TextView>(R.id.tvTotalQuestion)
        val tvCorrectValue = builder.findViewById<TextView>(R.id.tvCorrectValue)
        val tvIncorrectValue = builder.findViewById<TextView>(R.id.tvIncorrectValue)
        val tvNotAttempted = builder.findViewById<TextView>(R.id.tvNotAttempted)

        tvOk.setOnClickListener {
            callback.isEnabled = false
            builder.dismiss()
            activity?.onBackPressed()
        }

        val totalQuestion = questionsList.size
        val notSolved = totalQuestion - questionNumber

        tvTotalQuestion.text = totalQuestion.toString()
        tvCorrectValue.text = score.toString()
        tvIncorrectValue.text = inCorrect.toString()
        tvNotAttempted.text = notSolved.toString()


    }


    private fun showQuestion() {

        question = questionsList[questionNumber]
        binding.questionsTextView.text = question.question
        binding.tvOption1.text = question.option1
        binding.tvOption2.text = question.option2
        binding.tvOption3.text = question.option3
        binding.tvOption4.text = question.option4
        val number = questionNumber + 1
        "$number/10".also { binding.questionNumber.text = it }
        disableEnableCVOption(true)
        setCardBackgroundWhite()
        binding.progressbar.setProgress(number * 10)
        binding.waveView.setProgress(number * 10)
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
            ivOption.setImageResource(R.drawable.basic_tick)
            Log.d(TAG, "Correct Ans")
            score++
        } else {
            cvOption.background.setTint(Color.RED)
            ivOption.setImageResource(R.drawable.close)
            inCorrect++
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
                binding.ivOption1.setImageResource(R.drawable.basic_tick)
            }
            binding.tvOption2.text.toString() -> {
                binding.cvOption2.background.setTint(Color.GREEN)
                binding.ivOption2.setImageResource(R.drawable.basic_tick)
            }
            binding.tvOption3.text.toString() -> {
                binding.cvOption3.background.setTint(Color.GREEN)
                binding.ivOption3.setImageResource(R.drawable.basic_tick)
            }
            else -> {
                binding.cvOption4.background.setTint(Color.GREEN)
                binding.ivOption4.setImageResource(R.drawable.basic_tick)
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

