package com.shivtej.androidprojects

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.card.MaterialCardView
import com.shivtej.androidprojects.databinding.ActivityQuestionBinding
import com.shivtej.androidprojects.models.Question
import com.shivtej.androidprojects.utils.Constants
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_question.*

class QuestionActivity : AppCompatActivity() {

    private lateinit var questionList: ArrayList<Question>
    private lateinit var binding: ActivityQuestionBinding

    var questionNum = 0
    var totalQuestion = 0
    private var score = 0
    var onAfterQuizFragment = false

    private lateinit var selectedAnswer: String
    private var question = Question()
    private lateinit var selectedBtn: MaterialCardView
    private lateinit var selectedImg: CircleImageView
    lateinit var quizName: String


    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        questionList = ArrayList()
        selectedAnswer = ""



        questionList =
            intent.getParcelableArrayListExtra<Question>("QuestionList") as ArrayList<Question>
        quizName = intent.getStringExtra("quizName").toString()
        binding.tvQuizName.text = quizName
        totalQuestion = questionList.size - 1

        Log.i("Questions", questionList.toString())

        showQuestion()

        binding.questionToolbar.setNavigationOnClickListener {
            quitQuiz()
        }

        binding.tvQuit.setOnClickListener {

            quitQuiz()

        }


        binding.btnNext.setOnClickListener {
            if (selectedAnswer == "") {
                Toast.makeText(this, "Please Select Any one option", Toast.LENGTH_SHORT).show()
            } else {
                val text = btnNext.text.toString()
                Log.i("Submit", text)
                if (text == "Submit") {
                    btnNext.text = "Next"
                    checkAnswer(selectedBtn, selectedImg)
                }
                if (text == "Next") {
                    btnNext.text = "Submit"
                    nextQuestion()
                }
            }
        }

        binding.cvOption1.setOnClickListener {
            selectedAnswer = tvOption1.text.toString()
            selectedBtn = cvOption1
            selectedImg = ivOption1

            ivOption1.visibility = View.VISIBLE
            ivOption1.setImageResource(R.drawable.ic_check)
            ivOption2.visibility = View.INVISIBLE
            ivOption3.visibility = View.INVISIBLE
            ivOption4.visibility = View.INVISIBLE

            cvOption2.strokeColor = Color.BLACK
            cvOption3.strokeColor = Color.BLACK
            cvOption4.strokeColor = Color.BLACK


            cvOption2.setCardBackgroundColor(Color.BLACK)
            cvOption3.setCardBackgroundColor(Color.BLACK)
            cvOption4.setCardBackgroundColor(Color.BLACK)

            cvOption1.strokeColor = Color.parseColor("#06d3f6")
            cvOption1.setCardBackgroundColor(Color.WHITE)


        }

        binding.cvOption2.setOnClickListener {
            selectedAnswer = tvOption2.text.toString()
            selectedBtn = binding.cvOption2
            selectedImg = ivOption2
            ivOption2.visibility = View.VISIBLE
            ivOption2.setImageResource(R.drawable.ic_check)
            ivOption1.visibility = View.INVISIBLE
            ivOption3.visibility = View.INVISIBLE
            ivOption4.visibility = View.INVISIBLE

            cvOption1.strokeColor = Color.BLACK

            cvOption3.strokeColor = Color.BLACK

            cvOption4.strokeColor = Color.BLACK

            cvOption1.setCardBackgroundColor(Color.BLACK)
            cvOption3.setCardBackgroundColor(Color.BLACK)
            cvOption4.setCardBackgroundColor(Color.BLACK)




            cvOption2.strokeColor = Color.parseColor("#06d3f6")
            cvOption2.setCardBackgroundColor(Color.WHITE)


        }

        binding.cvOption3.setOnClickListener {
            selectedAnswer = tvOption3.text.toString()
            selectedBtn = binding.cvOption3
            selectedImg = ivOption3
            ivOption3.visibility = View.VISIBLE
            ivOption3.setImageResource(R.drawable.ic_check)
            ivOption1.visibility = View.INVISIBLE
            ivOption2.visibility = View.INVISIBLE
            ivOption4.visibility = View.INVISIBLE


            cvOption1.strokeColor = Color.BLACK

            cvOption2.strokeColor = Color.BLACK

            cvOption4.strokeColor = Color.BLACK
            cvOption4.setCardBackgroundColor(Color.BLACK)

            cvOption1.setCardBackgroundColor(Color.BLACK)
            cvOption2.setCardBackgroundColor(Color.BLACK)
            cvOption4.setCardBackgroundColor(Color.BLACK)

            cvOption3.strokeColor = Color.parseColor("#06d3f6")
            cvOption3.setCardBackgroundColor(Color.WHITE)


        }

        binding.cvOption4.setOnClickListener {
            selectedAnswer = tvOption4.text.toString()
            selectedBtn = binding.cvOption4
            selectedImg = ivOption4
            ivOption4.visibility = View.VISIBLE
            ivOption4.setImageResource(R.drawable.ic_check)
            ivOption1.visibility = View.INVISIBLE
            ivOption2.visibility = View.INVISIBLE
            ivOption3.visibility = View.INVISIBLE


            cvOption1.strokeColor = Color.BLACK
            cvOption1.setCardBackgroundColor(Color.BLACK)
            cvOption2.strokeColor = Color.BLACK
            cvOption2.setCardBackgroundColor(Color.BLACK)
            cvOption3.strokeColor = Color.BLACK
            cvOption3.setCardBackgroundColor(Color.BLACK)

            cvOption4.strokeColor = Color.parseColor("#06d3f6")
            cvOption4.setCardBackgroundColor(Color.WHITE)


        }
    }

    private fun quitQuiz() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Quit Quiz")
        builder.setMessage("Want to quit Quiz?\nYou will lose your score!")
        builder.setPositiveButton("Yes") { _: DialogInterface, i: Int ->
            finish()
        }
        builder.setNegativeButton("No") { _: DialogInterface, i: Int -> }

        builder.show()

    }

    private fun checkAnswer(selectedBtn: MaterialCardView, selectedImg: CircleImageView) {
        if (selectedAnswer == "") {
            Toast.makeText(this, "Please Select Any one option", Toast.LENGTH_SHORT).show()
        } else {
            if (selectedAnswer == question.answer) {
                selectedBtn.strokeColor = Color.GREEN
                // selectedBtn.setCardBackgroundColor(Color.GREEN)
                selectedImg.setImageResource(R.drawable.ic_correct)

                score++
                disabledCV()


            } else {
                selectedBtn.setCardBackgroundColor(Color.RED)
                selectedBtn.strokeColor = Color.RED
                selectedImg.setImageResource(R.drawable.ic_wrong)
                disabledCV()
                showCorrectAnswer()
            }
        }

    }

    private fun showCorrectAnswer() {
        when (question.answer) {
            tvOption1.text.toString() -> {
                cvOption1.strokeColor = Color.GREEN
                ivOption1.visibility = View.VISIBLE
                ivOption1.setImageResource(R.drawable.ic_correct)
            }
            tvOption2.text.toString() -> {
                cvOption2.strokeColor = Color.GREEN
                ivOption2.visibility = View.VISIBLE
                ivOption2.setImageResource(R.drawable.ic_correct)
            }
            tvOption3.text.toString() -> {
                cvOption3.strokeColor = Color.GREEN
                ivOption3.visibility = View.VISIBLE
                ivOption3.setImageResource(R.drawable.ic_correct)
            }
            tvOption4.text.toString() -> {
                cvOption4.strokeColor = Color.GREEN
                ivOption4.visibility = View.VISIBLE
                ivOption4.setImageResource(R.drawable.ic_correct)
            }
        }
    }

    private fun nextQuestion() {
        if (questionNum < totalQuestion) {
            Log.i("Count", questionNum.toString())
            Log.i("Size", questionList.size.toString())
            questionNum++

            showQuestion()
        } else {
            //Toast.makeText(this, "Quiz Completed", Toast.LENGTH_SHORT).show()
            binding.cl.visibility = View.GONE
            onAfterQuizFragment = true
            val fragment = AfterQuizFragment()
            val bundle = Bundle()
            Log.i("Score", score.toString())
            bundle.putInt(Constants.SCORE, score)
            bundle.putInt(Constants.TOTAL_SCORE, totalQuestion + 1)
            fragment.arguments = bundle
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fl, fragment).commit()

        }

    }

    private fun showQuestion() {

        question = questionList[questionNum]

        selectedAnswer = ""

        binding.tvQuestion.text = question.question
        binding.tvOption1.text = question.option1
        binding.tvOption2.text = question.option2
        binding.tvOption3.text = question.option3
        binding.tvOption4.text = question.option4

        cvOption1.setCardBackgroundColor(Color.BLACK)
        cvOption3.setCardBackgroundColor(Color.BLACK)
        cvOption2.setCardBackgroundColor(Color.BLACK)
        cvOption4.setCardBackgroundColor(Color.BLACK)

        cvOption1.strokeColor = Color.BLACK
        cvOption2.strokeColor = Color.BLACK
        cvOption3.strokeColor = Color.BLACK
        cvOption4.strokeColor = Color.BLACK

        ivOption1.visibility = View.INVISIBLE
        ivOption2.visibility = View.INVISIBLE
        ivOption3.visibility = View.INVISIBLE
        ivOption4.visibility = View.INVISIBLE

        enabledCV()

        val c = questionNum + 1
        val t = totalQuestion + 1
        val countString = "$c/$t"
        binding.tvQuestionNum.text = countString
    }

    private fun disabledCV() {
        cvOption1.isEnabled = false
        cvOption2.isEnabled = false
        cvOption3.isEnabled = false
        cvOption4.isEnabled = false
    }

    private fun enabledCV() {
        cvOption1.isEnabled = true
        cvOption2.isEnabled = true
        cvOption3.isEnabled = true
        cvOption4.isEnabled = true
    }

    override fun onBackPressed() {
        if(onAfterQuizFragment){
            finish()
        }else{
            quitQuiz()
        }
    }
}