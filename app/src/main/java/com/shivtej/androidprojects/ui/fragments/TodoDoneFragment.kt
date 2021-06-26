package com.shivtej.androidprojects.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.shivtej.androidprojects.adapters.TabLayoutAdapter
import com.shivtej.androidprojects.databinding.FragmentTodoDoneBinding
import com.shivtej.androidprojects.ui.MainActivity

class TodoDoneFragment : Fragment() {

    private lateinit var binding: FragmentTodoDoneBinding
    private lateinit var activity1: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoDoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity1 = activity as MainActivity
        activity1.projectView()

        val adapter = TabLayoutAdapter(fragmentManager, lifecycle)

        binding.todoViewPager.adapter = adapter

        TabLayoutMediator(binding.todoTabLayout, binding.todoViewPager){tab, position->
            when(position){
                0 -> tab.text = "Mark as Todo"
                1 -> tab.text = "Mark as Done"
            }
        }.attach()

    }

}