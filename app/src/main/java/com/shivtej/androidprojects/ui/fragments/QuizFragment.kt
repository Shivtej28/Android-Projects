package com.shivtej.androidprojects.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.adapters.QuizAdapter
import com.shivtej.androidprojects.adapters.QuizItemClicked
import com.shivtej.androidprojects.databinding.FragmentQuizBinding
import com.shivtej.androidprojects.ui.MainActivity
import com.shivtej.androidprojects.viewModels.ProjectViewModel

class QuizFragment: Fragment(), QuizItemClicked {

    private lateinit var binding: FragmentQuizBinding
    private lateinit var activity1: MainActivity
    private lateinit var navController: NavController
    private val viewModel: ProjectViewModel by activityViewModels()

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
        navController = Navigation.findNavController(view)

        val androidList = arrayOf("Android Quiz 1", "Android Quiz 2", "Android Quiz 3")
        val kotlinList = arrayOf("Kotlin Quiz 1", "Kotlin Quiz 2", "Kotlin Quiz 3")
        val javaList = arrayOf("Java Quiz 1", "Java Quiz 2", "Java Quiz 3")

        binding.androidRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.kotlinRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.javaRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val androidAdapter = QuizAdapter(androidList.toList(), this)
        binding.androidRecyclerView.adapter = androidAdapter

        val kotlinAdapter = QuizAdapter(kotlinList.toList(), this)
        binding.kotlinRecyclerView.adapter = kotlinAdapter

        val javaAdapter = QuizAdapter(javaList.toList(), this)
        binding.javaRecyclerView.adapter = javaAdapter
    }

    override fun onItemClicked(quiz: String) {
        val bundle = Bundle()
        bundle.putString("quizname", quiz)
        navController.navigate(R.id.action_quizFragment_to_questionFragment, bundle)
    }
}