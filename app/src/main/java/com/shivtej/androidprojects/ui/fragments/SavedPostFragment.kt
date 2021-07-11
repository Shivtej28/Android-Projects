package com.shivtej.androidprojects.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.adapters.LearnAdapter
import com.shivtej.androidprojects.adapters.OnClicked
import com.shivtej.androidprojects.databinding.FragmentSavedPostBinding
import com.shivtej.androidprojects.models.LearnBlog
import com.shivtej.androidprojects.viewModels.SavedPostViewModel

class SavedPostFragment : Fragment() {

    private lateinit var binding: FragmentSavedPostBinding

    private val viewModel: SavedPostViewModel by activityViewModels()
    private lateinit var navController: NavController
    private lateinit var adapter: LearnAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSavedPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setUpRecyclerView()

        viewModel.readAllPosts.observe(viewLifecycleOwner, Observer {
            adapter.getRoomPostList(it)
            adapter.setData(it)

        })
    }

    private fun setUpRecyclerView() {
        adapter = LearnAdapter(object : OnClicked {
            override fun onLearnBlogClicked(currentItem: LearnBlog) {
                val bundle = Bundle()
                bundle.putSerializable("blog", currentItem)
                navController.navigate(R.id.action_savedPostFragment_to_blogViewFragment, bundle)
            }

            override fun savePost(currentItem: LearnBlog) {

                viewModel.addPost(currentItem)
            }

            override fun deletePost(currentItem: LearnBlog) {
                viewModel.deletePost(currentItem)
            }


        })

        binding.savedLearnBlog.layoutManager = LinearLayoutManager(requireContext())
        binding.savedLearnBlog.adapter = adapter


    }
}