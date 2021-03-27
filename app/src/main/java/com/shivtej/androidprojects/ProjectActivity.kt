package com.shivtej.androidprojects

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.shivtej.androidprojects.databinding.ActivityProjectBinding
import com.shivtej.androidprojects.models.Project

class ProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.getBundleExtra("bundle")
        val project = bundle?.get("project") as Project

        Log.i("Project", project.toString())
        val title = project.title
        val description = project.description
        val apk = project.apk
        val zipfile = project.zipfile

//        binding.tvProjectTitle.text = title
//        binding.descriptionText.settings.allowContentAccess = true
//        binding.descriptionText.settings.allowFileAccess = true
//        binding.descriptionText.settings.allowUniversalAccessFromFileURLs = true
//        binding.descriptionText.settings.javaScriptEnabled = true
//        binding.descriptionText.settings.loadsImagesAutomatically = true
//        binding.descriptionText.loadUrl("https://raw.githubusercontent.com/Atharva-14/BMI_Calculator/master/README.md")


       // binding.descriptionText.loadMarkdownFile("https://raw.githubusercontent.com/Atharva-14/BMI_Calculator/master/README.md")



    }
}