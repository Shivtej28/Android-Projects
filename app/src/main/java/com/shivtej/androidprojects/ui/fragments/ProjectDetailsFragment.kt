package com.shivtej.androidprojects.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.shivtej.androidprojects.R
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity1 = activity as MainActivity

        project = arguments?.getSerializable("project") as Project
        Log.e("project", project.toString())

        val imagesList = getImagesList()
        activity1.showView()
        val detailUrl = project.description
        binding.webView.loadUrl(detailUrl)

        val adapter = SliderAdapter(imagesList)

        binding.imageSlider.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR

        binding.imageSlider.setSliderAdapter(adapter)

        binding.imageSlider.scrollTimeInSec = 3

        binding.imageSlider.isAutoCycle = true

        binding.imageSlider.startAutoCycle()


        binding.sourceCodeBtn.setOnClickListener {
            val builder = CustomTabsIntent.Builder()
            val colorSchemeParams = CustomTabColorSchemeParams.Builder()
                .setNavigationBarColor(ContextCompat.getColor(requireContext(), R.color.androidbg))
                .setToolbarColor(ContextCompat.getColor(requireContext(), R.color.orange))
                .setSecondaryToolbarColor(ContextCompat.getColor(requireContext(),R.color.primary))
                .build()
            builder.setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_DARK, colorSchemeParams)
            val customTabIntent = builder.build()

            customTabIntent.launchUrl(requireContext(), Uri.parse(project.zipfile))
        }


    }

    private fun getImagesList(): ArrayList<String> {
        val imagesList: ArrayList<String> = ArrayList()
        if (project.ss1 != "") {
            imagesList.add(project.ss1)
        }
        if (project.ss2 != "") {
            imagesList.add(project.ss2)
        }
        if (project.ss3 != "") {
            imagesList.add(project.ss3)
        }
        if (project.ss4 != "") {
            imagesList.add(project.ss4)
        }
        if (project.ss5 != "") {
            imagesList.add(project.ss5)
        }
        if (project.ss6 != "") {
            imagesList.add(project.ss1)
        }
        if (project.ss7 != "") {
            imagesList.add(project.ss7)
        }
        if (project.ss8 != "") {
            imagesList.add(project.ss8)
        }
        if (project.ss9 != "") {
            imagesList.add(project.ss9)
        }
        return imagesList
    }
}