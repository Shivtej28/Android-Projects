package com.shivtej.androidprojects

import android.R
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.request.target.Target
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
        val apptitle = project.title
        val description = project.description
        val apk = project.apk
        val zipfile = project.zipfile
        val image = project.image
        Glide.with(this).load(image).into(binding.ivProjectImage)

        binding.toolbar1.title = apptitle
        setSupportActionBar(binding.toolbar1)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        binding.tvProjectTitle.text = apptitle
        binding.descriptionText.text = description
//        binding.descriptionText.settings.allowContentAccess = true
//        binding.descriptionText.settings.allowFileAccess = true
//        binding.descriptionText.settings.allowUniversalAccessFromFileURLs = true
//        binding.descriptionText.settings.javaScriptEnabled = true
//        binding.descriptionText.settings.loadsImagesAutomatically = true
//        binding.descriptionText.loadUrl("https://raw.githubusercontent.com/Atharva-14/BMI_Calculator/master/README.md")


       // binding.descriptionText.loadMarkdownFile("https://raw.githubusercontent.com/Atharva-14/BMI_Calculator/master/README.md")



    }
}