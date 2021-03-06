package com.shivtej.androidprojects

 import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.shivtej.androidprojects.utils.Constants

class AfterQuizFragment : Fragment() {

    private lateinit var tvScore : TextView
    private lateinit var tvTotalScore : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_after_quiz, container, false)
        tvScore = view.findViewById(R.id.tvScore)
        tvTotalScore = view.findViewById(R.id.tvTotalScore)
        val tvCongrats: TextView = view.findViewById(R.id.tvCongrats)
        val tvMastered : TextView = view.findViewById(R.id.tvMastered)
        val ivwinLose : ImageView = view.findViewById(R.id.ivhappysad)
        val btnRestart : Button = view.findViewById(R.id.btnRestart)
        val btnShare : Button = view.findViewById(R.id.btnShare)

        val quizname = (activity as QuestionActivity).quizName

        btnRestart.setOnClickListener {
            (activity as QuestionActivity).finish()
        }

        btnShare.setOnClickListener {
            val appId = context?.packageName
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
            var shareMessage = "Hey Check Out My Score of $quizname on this App\n\n"
            shareMessage =
                """
                ${shareMessage}https://play.google.com/store/apps/details?id=$appId
                
                
                 """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        }

        val bundle = arguments
        val score = bundle?.getInt(Constants.SCORE)
        val totalQuestion = bundle?.getInt(Constants.TOTAL_SCORE)
        val percent = (score!! * 100)/ totalQuestion!!
        Log.i("Percent", percent.toString())
        Log.i("Score", score.toString())
        if(percent <= 50){
            tvScore.setTextColor(Color.RED)
            tvCongrats.text = getString(R.string.less_score_string)
            tvMastered.text = getString(R.string.you_have_to_practice_more)
            ivwinLose.setImageResource(R.drawable.lessscore)
        }else {
            tvScore.setTextColor(Color.GREEN)
            ivwinLose.setImageResource(R.drawable.highscore)
        }

        btnShare.setOnClickListener {
            val appId = context?.packageName
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
            var shareMessage = "Hey Check Out My Score of $quizname on this App\n\n"
            shareMessage =
                """
                ${shareMessage}https://play.google.com/store/apps/details?id=$appId
                
                
                 """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        }
        tvScore.text = "$score "
        tvTotalScore.text = "/ $totalQuestion"
        return view
    }


}