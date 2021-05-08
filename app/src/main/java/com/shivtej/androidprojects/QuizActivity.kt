package com.shivtej.androidprojects

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shivtej.androidprojects.adapters.BoxClicked
import com.shivtej.androidprojects.adapters.QuizBoardAdapter
import com.shivtej.androidprojects.databinding.ActivityQuizBinding
import com.shivtej.androidprojects.models.Question
import com.shivtej.androidprojects.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class QuizActivity : AppCompatActivity() {

    private lateinit var questionList: ArrayList<Question>
    private lateinit var quizName: String
    private lateinit var binding: ActivityQuizBinding
    private lateinit var myRef: DatabaseReference
    private lateinit var adapter: QuizBoardAdapter
    private lateinit var list: ArrayList<String>
    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var string1: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mInterstitialAd = InterstitialAd(this)
        MobileAds.initialize(this, Constants.mAPPUnitId)
        mInterstitialAd.adUnitId = Constants.testInterstitialId
        loadAd()

        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClicked() {
                super.onAdOpened()
                mInterstitialAd.adListener.onAdClosed()
            }

            override fun onAdClosed() {
                super.onAdClosed()
                startQuiz(string1)
            }
        }

        binding.boardView.visibility = View.VISIBLE
        list = ArrayList()
        quizName = intent.getStringExtra(Constants.QUIZ_NAME).toString()
        binding.toolbar.title = quizName
        binding.tvQuizName1.text = quizName
        setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        questionList = ArrayList()


        binding.boardView.layoutManager = LinearLayoutManager(this)
        adapter = QuizBoardAdapter(list, object : BoxClicked {
            override fun onBoxClicked(string: String) {
                string1 = string
                if (mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                    loadAd()
                } else {
                    startQuiz(string1)
                }
            }
        })
        binding.boardView.adapter = adapter
        binding.boardView.setHasFixedSize(true)

        fetchData()
    }

    private fun loadAd() {

        mInterstitialAd.loadAd(AdRequest.Builder().build())
    }

    override fun onResume() {
        super.onResume()
        binding.boardView.visibility = View.VISIBLE
    }

    private fun fetchData() {
        binding.simpleLoader.start()
        myRef = FirebaseDatabase.getInstance().getReference(quizName)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value: List<String> = snapshot.value as List<String>
                Log.i("Value", value.toString())
                for (i in value) {
                    if (i == null) {
                        Log.i("Null", "Null")
                    } else {
                        Log.i("Item", i)
                        list.add(i)
                        Log.i("List", list.toString())
                    }
                    adapter.notifyDataSetChanged()
                    binding.simpleLoader.stop()
                    binding.simpleLoader.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun startQuiz(string: String) {
        loadAd()
        val intent = Intent(this, QuestionActivity::class.java)
        binding.simpleLoader.visibility = View.VISIBLE
        binding.simpleLoader.start()
        questionList.clear()
        binding.boardView.visibility = View.GONE
        var quizname = ""
        quizname = if (quizName == "Kotlin Quiz") {
            "Kotlin $string"
        } else {
            "Android $string"
        }
        CoroutineScope(Dispatchers.IO).launch {
            val refer = Firebase.firestore.collection(quizname)
            try {
                val querySnapshot = refer.get().await()
                for (document in querySnapshot.documents) {
                    val question = document.toObject(Question::class.java)
                    if (question != null) {
                        questionList.add(question)
                    }
                }
                withContext(Dispatchers.Main) {
                    Log.i("QuestionList", questionList.toString())
                    binding.simpleLoader.stop()
                    binding.simpleLoader.visibility = View.GONE
                    intent.putParcelableArrayListExtra("QuestionList", questionList)
                    intent.putExtra("quizName", quizName)
                    startActivity(intent)

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@QuizActivity, e.message, Toast.LENGTH_SHORT).show()
                    binding.simpleLoader.stop()
                    binding.simpleLoader.visibility = View.GONE
                }
            }
        }

    }
}