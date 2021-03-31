package com.shivtej.androidprojects

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.recyclerview.widget.LinearLayoutManager
import com.shivtej.androidprojects.adapters.ItemClicked
import com.shivtej.androidprojects.adapters.ProjectsRVAdapter
import com.shivtej.androidprojects.databinding.ActivityMainBinding
import com.shivtej.androidprojects.models.ListGradient
import com.shivtej.androidprojects.utils.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val list = listOf("Basic", "Intermediate", "Advanced")

        val list :ArrayList<ListGradient>  = ArrayList()
        val listGradient1 = ListGradient("Basic", R.drawable.basicgradient)
        val listGradient2 = ListGradient("Intermediate", R.drawable.basicgradient)
        val listGradient3 = ListGradient("Advanced", R.drawable.basicgradient)
        list.add(listGradient1)
        list.add(listGradient2)
        list.add(listGradient3)

       // val quizList = listOf("Kotlin Quiz", "Android Quiz")
        val quiz1 = ListGradient("Kotlin Quiz", R.drawable.basicgradient)
        val quiz2 = ListGradient("Android Quiz", R.drawable.basicgradient)

        val quizList : ArrayList<ListGradient> = ArrayList()
        quizList.add(quiz1)
        quizList.add(quiz2)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvProjects.layoutManager = linearLayoutManager
        val projectsRVAdapter = ProjectsRVAdapter(list, object : ItemClicked{
            override fun onItemClicked(string: ListGradient) {
                val intent = Intent(this@MainActivity, ProjectListActivity::class.java)
                intent.putExtra(Constants.PROJECT_LEVEL, string.item)
                startActivity(intent)
            }
        })
        binding.rvProjects.adapter = projectsRVAdapter


        val quizManager = LinearLayoutManager(this)
        quizManager.orientation = LinearLayoutManager.HORIZONTAL

        val quizAdapter = ProjectsRVAdapter(quizList, object : ItemClicked{
            override fun onItemClicked(string: ListGradient) {
                val intent = Intent(this@MainActivity, QuizActivity::class.java)
                intent.putExtra(Constants.QUIZ_NAME, string.item)
                startActivity(intent)
            }

        })

        binding.rvQuizzes.layoutManager = quizManager
        binding.rvQuizzes.adapter = quizAdapter
    }
}