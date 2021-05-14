package com.shivtej.androidprojects

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.Toast

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestore.*
import com.google.firebase.firestore.ktx.*
import com.google.firebase.ktx.Firebase
import com.shivtej.androidprojects.adapters.ProjectItemClicked
import com.shivtej.androidprojects.adapters.ProjectListRVAdapter
import com.shivtej.androidprojects.ads.ShowAd
import com.shivtej.androidprojects.databinding.ActivityProjectListBinding


import com.shivtej.androidprojects.models.Project
import com.shivtej.androidprojects.utils.Constants
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class ProjectListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectListBinding
    private lateinit var level: String
    private lateinit var projectList: ArrayList<Project>
    private lateinit var adapter: ProjectListRVAdapter
    private lateinit var db: FirebaseFirestore


    var project1 = Project()
    //val showAd = ShowAd()
    lateinit var mInterstitialAd : InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mInterstitialAd = InterstitialAd(this)
        MobileAds.initialize(this, Constants.mAPPUnitId)
        mInterstitialAd.adUnitId = Constants.testInterstitialId

        //loadAd()
        println("UI Branch")
        println("UI Branch 2nd")
        mInterstitialAd.adListener = object : AdListener(){
            override fun onAdClicked() {
                super.onAdOpened()
                mInterstitialAd.adListener.onAdClosed()
            }

            override fun onAdClosed() {
                super.onAdClosed()
                gotoProjectActivity(project1)
            }
        }


        level = intent.getStringExtra(Constants.PROJECT_LEVEL).toString()
        projectList = ArrayList()
        db = Firebase.firestore
        binding.progressBar.start()
       setSupportActionBar(binding.materialToolbar1)
        binding.materialToolbar1.title = level
        binding.tvHeading.text = level
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)

      //  binding.materialToolbar2.setNavigationIconTint(R.drawable.ic_back_arrow)
        binding.materialToolbar1.setNavigationOnClickListener {
            onBackPressed()
        }


        val layoutManager = LinearLayoutManager(this)
        binding.rvProjectList.layoutManager = layoutManager

        adapter = ProjectListRVAdapter(projectList, object : ProjectItemClicked {
            override fun onProjectClicked(project: Project) {
                project1 = project
                  if(mInterstitialAd.isLoaded){
                      mInterstitialAd.show()

                  }else {
                      gotoProjectActivity(project1)
                 }
                loadAd()
                 }
        })
        binding.rvProjectList.adapter = adapter
        getProjectList()
    }

    private fun loadAd() {
        mInterstitialAd.loadAd(AdRequest.Builder().build())
    }

    private fun gotoProjectActivity(project: Project) {
        val intent = Intent(this@ProjectListActivity, ProjectActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(Constants.PROJECT, project)
        intent.putExtra(Constants.BUNDLE, bundle)
        startActivity(intent)

    }

    private fun runAdEvents() {

    }

//    private fun loadInterstitialAd(mInterstitialAdUnitId: String) {
//            mInterstitialAd.adUnitId = mInterstitialAdUnitId
//            mInterstitialAd.loadAd(AdRequest.Builder().build())
//    }
//
//    private fun initializeInterstitialAd(mAPPUnitId: String) {
//            MobileAds.initialize(this, mAPPUnitId)
//    }

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
                binding.progressBar.stop()
                binding.progressBar.visibility = View.INVISIBLE

            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@ProjectListActivity, "Can't get Data", Toast.LENGTH_SHORT)
                    .show()
                binding.progressBar.stop()
                binding.progressBar.visibility = View.INVISIBLE

            }
        }
    }

}




