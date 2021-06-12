package com.shivtej.androidprojects.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shivtej.androidprojects.databinding.FragmentProjectDetailsBinding
import com.smarteist.autoimageslider.SliderView

class ProjectDetailsFragment: Fragment() {

    private lateinit var binding: FragmentProjectDetailsBinding

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

        //creating arraylist for storing images

        //adding urls inside array list

        //passing arraylist to adapter class

        binding.imageSlider.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR

        //Set Adapter

        binding.imageSlider.scrollTimeInSec = 3

        binding.imageSlider.isAutoCycle = true

        binding.imageSlider.startAutoCycle()

    }
}