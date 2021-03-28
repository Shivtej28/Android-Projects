package com.shivtej.androidprojects

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestore.*
import com.google.firebase.firestore.ktx.*
import com.google.firebase.ktx.Firebase
import com.shivtej.androidprojects.adapters.ProjectItemClicked
import com.shivtej.androidprojects.adapters.ProjectListRVAdapter
import com.shivtej.androidprojects.databinding.ActivityProjectListBinding


import com.shivtej.androidprojects.models.Project
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class ProjectListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectListBinding
    private lateinit var level: String
    private lateinit var projectList: ArrayList<Project>
    private lateinit var adapter: ProjectListRVAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        level = intent.getStringExtra("level").toString()
        projectList = ArrayList()
        db = Firebase.firestore
        binding.progressBar.visibility = View.VISIBLE
        setSupportActionBar(binding.materialToolbar2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        Log.i("List", projectList.toString())

        val layoutManager = LinearLayoutManager(this)
        binding.rvProjectList.layoutManager = layoutManager

        adapter = ProjectListRVAdapter(projectList, object : ProjectItemClicked {
            override fun onProjectClicked(project: Project) {
                val intent = Intent(this@ProjectListActivity, ProjectActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable("project", project)
                intent.putExtra("bundle", bundle)
                startActivity(intent)
            }
        })
        binding.rvProjectList.adapter = adapter
        getProjectList()
    }

    private fun getProjectList() = CoroutineScope(Dispatchers.IO).launch {
        val refer = db.collection(level)
        try {
            val querySnapshot = refer.get().await()
            for (document in querySnapshot.documents) {
                val project = document.toObject(Project::class.java)
                if (project != null) {
                    projectList.add(project)
                }
            }
            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.INVISIBLE
                Log.i("Main", projectList.toString())
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@ProjectListActivity, "Can't get Data", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.INVISIBLE
            }
        }
    }
}




