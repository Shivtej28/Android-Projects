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
import com.shivtej.androidprojects.adapters.OnOptionClicked
import com.shivtej.androidprojects.adapters.QuestionAdapter
import com.shivtej.androidprojects.databinding.FragmentQuestionBinding
import com.shivtej.androidprojects.ui.MainActivity
import com.shivtej.androidprojects.models.Question
import com.shivtej.androidprojects.viewModels.ProjectViewModel
import de.hdodenhof.circleimageview.CircleImageView

class QuestionFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentQuestionBinding
    private lateinit var includeView: View
    private lateinit var activity1: MainActivity
    private val TAG = "QuestionFragment"
    private val viewModel: ProjectViewModel by activityViewModels()
    private lateinit var questionsList: List<Question>
    private lateinit var question: Question

    private var questionNumber = 0
    private var score = 0
    private var inCorrect = 0
    private var notAttempted = 0

    private lateinit var callback: OnBackPressedCallback

//    private lateinit var tvOption1: TextView
//    private lateinit var tvOption2: TextView
//    private lateinit var tvOption3: TextView
//    private lateinit var tvOption4: TextView
//    private lateinit var cvOption1: MaterialCardView
//    private lateinit var cvOption2: MaterialCardView
//    private lateinit var cvOption3: MaterialCardView
//    private lateinit var cvOption4: MaterialCardView
//    private lateinit var ivOption1: CircleImageView
//    private lateinit var ivOption2: CircleImageView
//    private lateinit var ivOption3: CircleImageView
//    private lateinit var ivOption4: CircleImageView


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
        activity1 = activity as MainActivity
        activity1.hideView()

        binding.tvQuizName.text = quizname

        questionsList = arrayListOf()

        callback = requireActivity().onBackPressedDispatcher.addCallback {
            Log.d("back", "back")
            showQuitDialog()
        }
        callback.isEnabled = true

        viewModel.getQuestions(quizname).observe(viewLifecycleOwner, {
            questionsList = it
            showQuestion()

        })




        binding.includeLayout.cvOption1.setOnClickListener(this)
        binding.includeLayout.cvOption2.setOnClickListener(this)
        binding.includeLayout.cvOption3.setOnClickListener(this)
        binding.includeLayout.cvOption4.setOnClickListener(this)
    }

    private fun showQuitDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle("End Quiz?")
            .setPositiveButton("Yes") { _, _ ->
                showScoreDialog()
            }
            .setNegativeButton("No") { _, _ ->

            }

        val dialog = dialogBuilder.create()

        dialog.show()
    }

    private fun showScoreDialog() {
        val builder =
            Dialog(requireContext(), R.style.Base_ThemeOverlay_MaterialComponents_Dialog_Alert)
        builder.setContentView(R.layout.quiz_complete_dialog_box)
        builder.show()
        builder.setCanceledOnTouchOutside(false)

        val tvTotalQuestion: TextView = builder.findViewById(R.id.tvTotalQuestion)
        val tvCorrect: TextView = builder.findViewById(R.id.tvCorrectValue)
        val tvIncorrect: TextView = builder.findViewById(R.id.tvIncorrectValue)
        val tvNotAttempted: TextView = builder.findViewById(R.id.tvNotAttempted)
        tvCorrect.text = score.toString()
        val total = questionsList.size
        tvTotalQuestion.text = total.toString()
        tvIncorrect.text = inCorrect.toString()

        val notAttempted = total - (score + inCorrect)
        tvNotAttempted.text = notAttempted.toString()

        val tvOk: TextView = builder.findViewById(R.id.tvOk)

        tvOk.setOnClickListener {
            callback.isEnabled = false
            builder.dismiss()
            activity?.onBackPressed()
        }

    }


//    override fun onClicked(view: View, currentItem: Question, num: Int, position: Int) {
//
//        Log.i("Ans", currentItem.answer)
//
//        question = currentItem
//        tvOption1 = view.findViewById(R.id.tvOption1)
//        tvOption2 = view.findViewById(R.id.tvOption2)
//        tvOption3 = view.findViewById(R.id.tvOption3)
//        tvOption4 = view.findViewById(R.id.tvOption4)
//
//        cvOption1 = view.findViewById(R.id.cvOption1)
//        cvOption2 = view.findViewById(R.id.cvOption2)
//        cvOption3 = view.findViewById(R.id.cvOption3)
//        cvOption4 = view.findViewById(R.id.cvOption4)
//
//        ivOption1 = view.findViewById(R.id.ivOption1)
//        ivOption2 = view.findViewById(R.id.ivOption2)
//        ivOption3 = view.findViewById(R.id.ivOption3)
//        ivOption4 = view.findViewById(R.id.ivOption4)
//
//        when (num) {
//            1 -> checkAnswer(tvOption1, cvOption1, ivOption1)
//            2 -> checkAnswer(tvOption2, cvOption2, ivOption2)
//            3 -> checkAnswer(tvOption3, cvOption3, ivOption3)
//            4 -> checkAnswer(tvOption4, cvOption4, ivOption4)
//        }
//
//        Log.i("Ans", tvOption1.text.toString())
//
//    }

    private fun showQuestion() {

        question = questionsList[questionNumber]
        binding.includeLayout.questionsTextView.text = question.question
        binding.includeLayout.tvOption1.text = question.option1
        binding.includeLayout.tvOption2.text = question.option2
        binding.includeLayout.tvOption3.text = question.option3
        binding.includeLayout.tvOption4.text = question.option4
        val number = questionNumber + 1
        "$number/10".also { binding.questionNumber.text = it }
        disableEnableCVOption(true)
        setCardBackgroundWhite()
        binding.progressbar.setProgress(number * 10)
        binding.waveView.setProgress(number * 10)
    }

    private fun setCardBackgroundWhite() {

        binding.includeLayout.cvOption1.background.setTint(Color.WHITE)
        binding.includeLayout.ivOption1.setImageResource(R.drawable.a_option)
        binding.includeLayout.cvOption2.background.setTint(Color.WHITE)
        binding.includeLayout.ivOption2.setImageResource(R.drawable.b_option)
        binding.includeLayout.cvOption3.background.setTint(Color.WHITE)
        binding.includeLayout.ivOption3.setImageResource(R.drawable.c_option)
        binding.includeLayout.cvOption4.background.setTint(Color.WHITE)
        binding.includeLayout.ivOption4.setImageResource(R.drawable.d_option)
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.cvOption1 -> {
                checkAnswer(
                    binding.includeLayout.tvOption1,
                    binding.includeLayout.cvOption1,
                    binding.includeLayout.ivOption1
                )
            }
            R.id.cvOption2 -> {
                checkAnswer(
                    binding.includeLayout.tvOption2,
                    binding.includeLayout.cvOption2,
                    binding.includeLayout.ivOption2
                )
            }
            R.id.cvOption3 -> {
                checkAnswer(
                    binding.includeLayout.tvOption3,
                    binding.includeLayout.cvOption3,
                    binding.includeLayout.ivOption3
                )
            }
            R.id.cvOption4 -> {
                checkAnswer(
                    binding.includeLayout.tvOption4,
                    binding.includeLayout.cvOption4,
                    binding.includeLayout.ivOption4
                )
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
            score++
            Log.d(TAG, "Correct Ans")
        } else {
            cvOption.background.setTint(Color.RED)
            ivOption.setImageResource(R.drawable.close)
            inCorrect++
            getCorrectAnswer()
        }
        if (questionNumber < questionsList.size - 1) {
            questionNumber++
            showQuestion()
        } else {
            showScoreDialog()
        }


    }

    private fun getCorrectAnswer() {
        when (question.answer) {
            binding.includeLayout.tvOption1.text.toString() -> {
                binding.includeLayout.cvOption1.background.setTint(Color.GREEN)
                binding.includeLayout.ivOption1.setImageResource(R.drawable.basic_tick)
            }
            binding.includeLayout.tvOption2.text.toString() -> {
                binding.includeLayout.cvOption2.background.setTint(Color.GREEN)
                binding.includeLayout.ivOption2.setImageResource(R.drawable.basic_tick)
            }
            binding.includeLayout.tvOption3.text.toString() -> {
                binding.includeLayout.cvOption3.background.setTint(Color.GREEN)
                binding.includeLayout.ivOption3.setImageResource(R.drawable.basic_tick)
            }
            else -> {
                binding.includeLayout.cvOption4.background.setTint(Color.GREEN)
                binding.includeLayout.ivOption4.setImageResource(R.drawable.basic_tick)
            }
        }
    }

    private fun disableEnableCVOption(b: Boolean) {
        binding.includeLayout.cvOption1.isEnabled = b
        binding.includeLayout.cvOption2.isEnabled = b
        binding.includeLayout.cvOption3.isEnabled = b
        binding.includeLayout.cvOption4.isEnabled = b
    }


}