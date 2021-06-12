package com.shivtej.androidprojects.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shivtej.androidprojects.databinding.FragmentProjectDetailsBinding
import com.shivtej.androidprojects.models.Project
import com.shivtej.androidprojects.ui.MainActivity
import com.smarteist.autoimageslider.SliderView

class ProjectDetailsFragment: Fragment() {

    private lateinit var binding: FragmentProjectDetailsBinding
    private lateinit var project: Project
    private lateinit var activity1 : MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProjectDetailsBinding.inflate(inflater, container, false)
        activity1 = activity as MainActivity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity1.hideView()
        project = arguments?.getSerializable("project") as Project
        Log.e("project", project.toString())
        val imagesList = getImagesList()

        val detailUrl = project.description
        binding.webView.loadUrl(detailUrl)
        //creating arraylist for storing images

        //adding urls inside array list

        //passing arraylist to adapter class


        binding.imageSlider.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR

        //Set Adapter

        binding.imageSlider.scrollTimeInSec = 3

        binding.imageSlider.isAutoCycle = true

        binding.imageSlider.startAutoCycle()

    }

    private fun getImagesList(): ArrayList<String> {
        val imagesList: ArrayList<String> = ArrayList()
        if (!project.ss1.equals("")) {
            imagesList.add(project.ss1)
        }
        if (!project.ss2.equals("")) {
            imagesList.add(project.ss1)
        }
        if (!project.ss3.equals("")) {
            imagesList.add(project.ss1)
        }
        if (!project.ss4.equals("")) {
            imagesList.add(project.ss1)
        }

        if (!project.ss5.equals("")) {
            imagesList.add(project.ss1)
        }
        if (!project.ss6.equals("")) {
            imagesList.add(project.ss1)
        }
        if (!project.ss7.equals("")) {
            imagesList.add(project.ss1)
        }
        if (!project.ss8.equals("")) {
            imagesList.add(project.ss1)
        }
        if (!project.ss9.equals("")) {
            imagesList.add(project.ss1)
        }

        return imagesList
    }
}