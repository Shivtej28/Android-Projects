package com.shivtej.androidprojects.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.adapters.LearnAdapter
import com.shivtej.androidprojects.adapters.OnClicked
import com.shivtej.androidprojects.databinding.FragmentLearnBinding
import com.shivtej.androidprojects.models.LearnBlog
import com.shivtej.androidprojects.models.Project
import com.shivtej.androidprojects.ui.MainActivity
import com.shivtej.androidprojects.viewModels.ProjectViewModel

class LearnFragment: Fragment(), OnClicked {

    private lateinit var binding: FragmentLearnBinding
    private lateinit var activity1: MainActivity

    private lateinit var learnBlogList: List<LearnBlog>
    private lateinit var adapter: LearnAdapter

    private lateinit var navController: NavController

    private val viewModel: ProjectViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLearnBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity1 = activity as MainActivity
        activity1.showView()
        navController = Navigation.findNavController(view)

        learnBlogList = listOf()

        binding.learnRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getLearnBlog().observe(viewLifecycleOwner, Observer {
            learnBlogList = it
            adapter = LearnAdapter(learnBlogList, this)
            binding.learnRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()

        })

    }

    override fun onLearnBlogClicked(currentItem: LearnBlog) {
        val bundle = Bundle()
        bundle.putSerializable("blog", currentItem)
        navController.navigate(R.id.action_learnFragment_to_blogViewFragment, bundle)
    }

    override fun onMenuMarkAsTodoClicked(currentItem: LearnBlog) {
        TODO("Not yet implemented")
    }

    override fun orMenuMarkAsDoneClicked(currentItem: LearnBlog) {
        TODO("Not yet implemented")
    }
}