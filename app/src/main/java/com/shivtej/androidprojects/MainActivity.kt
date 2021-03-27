package com.shivtej.androidprojects

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.recyclerview.widget.LinearLayoutManager
import com.shivtej.androidprojects.adapters.ItemClicked
import com.shivtej.androidprojects.adapters.ProjectsRVAdapter
import com.shivtej.androidprojects.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list = listOf("Basic", "Intermediate", "Advanced")
        val quizList = listOf("Kotlin Quiz", "Android Quiz")

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvProjects.layoutManager = linearLayoutManager
        val projectsRVAdapter = ProjectsRVAdapter(list, object : ItemClicked{
            override fun onItemClicked(level: String) {
                val intent = Intent(this@MainActivity, ProjectListActivity::class.java)
                intent.putExtra("level", level)
                startActivity(intent)
            }
        })
        binding.rvProjects.adapter = projectsRVAdapter


        val quizManager = LinearLayoutManager(this)
        quizManager.orientation = LinearLayoutManager.HORIZONTAL

        val quizAdapter = ProjectsRVAdapter(quizList, object : ItemClicked{
            override fun onItemClicked(string: String) {
                val intent = Intent(this@MainActivity, QuizActivity::class.java)
                intent.putExtra("quizName", string)
                startActivity(intent)
            }

        })

        binding.rvQuizzes.layoutManager = quizManager
        binding.rvQuizzes.adapter = quizAdapter





    }


}