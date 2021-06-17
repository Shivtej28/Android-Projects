package com.shivtej.androidprojects.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shivtej.androidprojects.databinding.FragmentQuestionBinding
import com.shivtej.androidprojects.ui.MainActivity

class QuestionFragment: Fragment() {

    private lateinit var binding: FragmentQuestionBinding
    private lateinit var activity1: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionBinding.inflate(inflater, container, false)
        activity1 = activity as MainActivity
        activity1.hideView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}