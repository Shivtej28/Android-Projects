package com.shivtej.androidprojects.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.shivtej.androidprojects.databinding.FragmentQuizBinding
import com.shivtej.androidprojects.ui.MainActivity

class QuizFragment: Fragment() {

    private lateinit var binding: FragmentQuizBinding
    private lateinit var activity1: MainActivity
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        activity1 = activity as MainActivity
        activity1.showView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}