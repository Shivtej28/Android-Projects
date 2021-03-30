package com.shivtej.androidprojects

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import com.shivtej.androidprojects.adapters.BoxClicked
import com.shivtej.androidprojects.adapters.QuizBoardAdapter
import com.shivtej.androidprojects.databinding.ActivityQuizBinding
import com.shivtej.androidprojects.models.Question
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class QuizActivity : AppCompatActivity() {

    private lateinit var questionList : ArrayList<Question>
    private lateinit var quizName : String
    private lateinit var binding: ActivityQuizBinding
    private lateinit var myRef : DatabaseReference
    private lateinit var adapter: QuizBoardAdapter
    private lateinit var list : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.boardView.visibility = View.VISIBLE
        list = ArrayList()
         quizName = intent.getStringExtra("quizName").toString()
        binding.toolbar.title = quizName
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        questionList = ArrayList()


        binding.boardView.layoutManager = LinearLayoutManager(this)
        adapter = QuizBoardAdapter(this, list, object :BoxClicked{
            override fun onBoxClicked(string: String) {
                startQuiz(string)
            }
        })
        binding.boardView.adapter = adapter
        binding.boardView.setHasFixedSize(true)

        fetchData()
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

                val value: List<String> = snapshot.getValue() as List<String>
                Log.i("Value", value.toString())
                for (i in value) {
                    if (i == null) {
                        Log.i("Null", "Null")
                    } else {
                        Log.i("Item", i.toString())
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
//        binding.progressBar.visibility = View.VISIBLE

//
 }

    fun startQuiz(string: String) {
        val intent = Intent(this, QuestionActivity::class.java)
        //binding.boardView.visibility = View.GONE
        binding.simpleLoader.visibility = View.VISIBLE
        binding.simpleLoader.start()
        questionList.clear()
        binding.boardView.visibility = View.GONE

        var quizname = ""
        if(quizName == "Kotlin Quiz"){
            quizname = "Kotlin $string"
        }else{
            quizname = "Android $string"
        }
        CoroutineScope(Dispatchers.IO).launch {
            val refer = Firebase.firestore.collection(quizname)
            try {
                val querySnapshot = refer.get().await()
                for(document in querySnapshot.documents){
                    val question = document.toObject(Question::class.java)
                    if (question != null) {
                        questionList.add(question)
                    }
                }
                //Log.i("QuestionList", questionList.toString())
                withContext(Dispatchers.Main){
                    Log.i("QuestionList", questionList.toString())
                    binding.simpleLoader.stop()
                    binding.simpleLoader.visibility = View.GONE
                    intent.putParcelableArrayListExtra("QuestionList", questionList)
                    intent.putExtra("quizName", quizName)
                    startActivity(intent)

                }
            }catch (e : Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@QuizActivity, e.message, Toast.LENGTH_SHORT).show()
                    binding.simpleLoader.stop()
                    binding.simpleLoader.visibility = View.GONE

                }
            }
       }

    }
}