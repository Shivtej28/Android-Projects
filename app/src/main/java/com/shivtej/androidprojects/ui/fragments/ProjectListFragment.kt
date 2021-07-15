package com.shivtej.androidprojects.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.adapters.ItemClicked
import com.shivtej.androidprojects.adapters.ProjectAdapter
import com.shivtej.androidprojects.databinding.FragmentProjectListBinding
import com.shivtej.androidprojects.models.Project
import com.shivtej.androidprojects.ui.MainActivity

class ProjectListFragment : Fragment() {

    private lateinit var binding: FragmentProjectListBinding
    private lateinit var activity1: MainActivity
    private val TAG = "ProjectListFragment"

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProjectListBinding.inflate(inflater, container, false)
        activity1 = activity as MainActivity
//        activity1.findViewById<MaterialToolbar>(R.id.toolbar).visibility = View.VISIBLE
//        activity1.findViewById<BottomNavigationView>(R.id.bottom_nav_bar).visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        val list = ProjectListFragmentArgs.fromBundle(requireArguments())
        activity1.hideView()
        setUpRecyclerView(list)

        binding.toolbarTextView.text = list.projectName


    }

    private fun setUpRecyclerView(list: ProjectListFragmentArgs) {
        val adapter = ProjectAdapter(list.projectList.toList(), object : ItemClicked {
            override fun onItemClicked(project: Project) {
                navController.navigate(
                    ProjectListFragmentDirections.actionProjectListFragmentToProjectDetailsFragment(
                        project
                    )
                )
            }

        })
        binding.projectListRecyclerView.adapter = adapter
        binding.projectListRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

    }
}