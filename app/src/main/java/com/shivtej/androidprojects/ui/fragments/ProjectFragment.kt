package com.shivtej.androidprojects.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.databinding.FragmentProjectBinding
import com.shivtej.androidprojects.ui.MainActivity

class ProjectFragment: Fragment() {

    private lateinit var binding: FragmentProjectBinding
    private lateinit var activity1: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProjectBinding.inflate(inflater, container, false)
        activity1 = activity as MainActivity
        activity1.findViewById<MaterialToolbar>(R.id.toolbar).visibility = View.VISIBLE
        activity1.findViewById<BottomNavigationView>(R.id.bottom_nav_bar).visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}