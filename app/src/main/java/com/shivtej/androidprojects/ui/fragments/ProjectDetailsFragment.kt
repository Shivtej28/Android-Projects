package com.shivtej.androidprojects.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shivtej.androidprojects.adapters.SliderAdapter
import com.shivtej.androidprojects.databinding.FragmentProjectDetailsBinding
import com.shivtej.androidprojects.models.Project
import com.shivtej.androidprojects.ui.MainActivity
import com.smarteist.autoimageslider.SliderView

class ProjectDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProjectDetailsBinding
    private lateinit var project: Project
    private lateinit var activity1: MainActivity

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

        val adapter = SliderAdapter(imagesList)

        binding.imageSlider.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR

        binding.imageSlider.setSliderAdapter(adapter)

        binding.imageSlider.scrollTimeInSec = 3

        binding.imageSlider.isAutoCycle = true

        binding.imageSlider.startAutoCycle()

    }

    private fun getImagesList(): ArrayList<String> {
        val imagesList: ArrayList<String> = ArrayList()
        if (project.ss1 != "") {
            imagesList.add(project.ss1)
        }
        if (project.ss2 != "") {
            imagesList.add(project.ss1)
        }
        if (project.ss3 != "") {
            imagesList.add(project.ss1)
        }
        if (project.ss4 != "") {
            imagesList.add(project.ss1)
        }

        if (project.ss5 != "") {
            imagesList.add(project.ss1)
        }
        if (project.ss6 != "") {
            imagesList.add(project.ss1)
        }
        if (project.ss7 != "") {
            imagesList.add(project.ss1)
        }
        if (project.ss8 != "") {
            imagesList.add(project.ss1)
        }
        if (project.ss9 != "") {
            imagesList.add(project.ss1)
        }

        return imagesList
    }
}