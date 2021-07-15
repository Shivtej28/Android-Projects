package com.shivtej.androidprojects.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.adapters.ItemClicked
import com.shivtej.androidprojects.adapters.ProjectAdapter
import com.shivtej.androidprojects.databinding.FragmentProjectBinding
import com.shivtej.androidprojects.models.Project
import com.shivtej.androidprojects.ui.MainActivity
import com.shivtej.androidprojects.viewModels.ProjectViewModel
import kotlin.random.Random

class ProjectFragment : Fragment(), ItemClicked {

    private lateinit var binding: FragmentProjectBinding
    private lateinit var activity1: MainActivity
    private val viewModel: ProjectViewModel by activityViewModels()

    private lateinit var basicProjectsList: List<Project>
    private lateinit var intermediateProjectsList: List<Project>
    private lateinit var advanceProjectList: List<Project>
    private lateinit var basicAdapter: ProjectAdapter
    private lateinit var intermediateAdapter: ProjectAdapter
    private lateinit var advancedAdapter: ProjectAdapter
    // var user: User? = null

    private lateinit var navController: NavController
    private val TAG = "ProjectFragment"

    private var pressedTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProjectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        basicProjectsList = listOf()
        intermediateProjectsList = listOf()
        advanceProjectList = listOf()
        activity1 = activity as MainActivity
        activity1.showView()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    closeApp()
                }

            })
        //callback.isEnabled = true
        binding.basicRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.intermediateRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.advanceRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        viewModel.getBasicProjects().observe(viewLifecycleOwner, {
            basicProjectsList = it
            basicAdapter = ProjectAdapter(basicProjectsList, this)
            binding.basicRecyclerView.adapter = basicAdapter

            Log.d(TAG, it.toString())
            basicAdapter.notifyDataSetChanged()
        })

        viewModel.getIntermediateProjects().observe(viewLifecycleOwner, {
            intermediateProjectsList = it
            intermediateAdapter = ProjectAdapter(intermediateProjectsList, this)
            binding.intermediateRecyclerView.adapter = intermediateAdapter
            intermediateAdapter.notifyDataSetChanged()
        })

        viewModel.getAdvanceProjects().observe(viewLifecycleOwner, {
            advanceProjectList = it
            advancedAdapter = ProjectAdapter(advanceProjectList, this)
            binding.advanceRecyclerView.adapter = advancedAdapter
            advancedAdapter.notifyDataSetChanged()
        })

        //getRandomText()

        binding.basicLinearLayout.setOnClickListener {
            val text = binding.tvBasic.text.toString()
            goToProjectListFragment(basicProjectsList, text)
        }

        binding.intermediateLinearLayout.setOnClickListener {
            val text = binding.tvIntermediate.text.toString()
            goToProjectListFragment(intermediateProjectsList, text)
        }

        binding.advanceLinearLayout.setOnClickListener {
            val text = binding.tvAdvanced.text.toString()
            goToProjectListFragment(advanceProjectList, text)
        }
    }

    private fun goToProjectListFragment(projectList: List<Project>, text: String) {
        navController
            .navigate(
                ProjectFragmentDirections.actionProjectFragmentToProjectListFragment(
                    projectList.toTypedArray(), text
                )
            )
    }

    private fun getRandomText() {
        val randomToolbarText =
            arrayOf("Hi,", "Hello,", "Namaste,\uD83D\uDE4F", "Hola,", "Hey,", "How's it going?")
        val randomValue = Random.nextInt(randomToolbarText.size)

        activity1.findViewById<TextView>(R.id.toolbar_text_view).text =
            randomToolbarText[randomValue]
    }

    private fun closeApp() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            activity?.finish()
        } else {
            Toast.makeText(requireContext(), "Press again to exit", Toast.LENGTH_SHORT)
                .show()
            Log.d("finish", activity1.supportFragmentManager.backStackEntryCount.toString())
        }
        pressedTime = System.currentTimeMillis()
    }

    override fun onItemClicked(project: Project) {
        val bundle = Bundle()
        bundle.putSerializable("project", project)
        navController.navigate(
            ProjectFragmentDirections.actionProjectFragmentToProjectDetailsFragment(
                project
            )
        )
    }


}