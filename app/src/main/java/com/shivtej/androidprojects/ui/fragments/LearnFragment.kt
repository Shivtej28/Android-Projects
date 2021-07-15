package com.shivtej.androidprojects.ui.fragments

import android.os.Bundle
import android.util.Log
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
import com.google.android.material.snackbar.Snackbar
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.adapters.LearnAdapter
import com.shivtej.androidprojects.adapters.OnClicked
import com.shivtej.androidprojects.databinding.FragmentLearnBinding
import com.shivtej.androidprojects.models.LearnBlog
import com.shivtej.androidprojects.ui.MainActivity
import com.shivtej.androidprojects.utils.RetrofitClient
import com.shivtej.androidprojects.viewModels.ProjectViewModel
import com.shivtej.androidprojects.viewModels.SavedPostViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LearnFragment : Fragment() {

    private lateinit var binding: FragmentLearnBinding
    private lateinit var activity1: MainActivity

    private lateinit var learnBlogList: List<LearnBlog>
    private lateinit var adapter: LearnAdapter

    private lateinit var navController: NavController


    private val viewModel: SavedPostViewModel by activityViewModels()

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
       activity1.checkNetwork()
        navController = Navigation.findNavController(view)

        learnBlogList = emptyList()

        // getLearnBlogs()

//        viewModel.getLearnBlog().observe(viewLifecycleOwner, Observer {
//            learnBlogList = it
//            adapter = LearnAdapter(learnBlogList, this)
//            binding.learnRecyclerView.adapter = adapter
//            adapter.notifyDataSetChanged()
//
//        })


    }

    override fun onStart() {
        super.onStart()
        setUpRecyclerView()
        getLearnBlogs()
        viewModel.readAllPosts.observe(viewLifecycleOwner, Observer {
            adapter.getRoomPostList(it)

        })


    }


    private fun setUpRecyclerView() {
        adapter = LearnAdapter(object : OnClicked {
            override fun onLearnBlogClicked(currentItem: LearnBlog) {
                val bundle = Bundle()
                bundle.putSerializable("blog", currentItem)
                navController.navigate(R.id.action_learnFragment_to_blogViewFragment, bundle)
            }

            override fun savePost(currentItem: LearnBlog) {
                viewModel.addPost(currentItem)
                Snackbar.make(binding.root, "Saved Blog", Snackbar.LENGTH_SHORT).show()

                onStart()
            }

            override fun deletePost(currentItem: LearnBlog) {
                viewModel.deletePost(currentItem)

                val snackbar = Snackbar.make(binding.root, "Deleted Blog", Snackbar.LENGTH_SHORT)
                    .setAction("UNDO") {
                        viewModel.addPost(currentItem)
                        onStart()
                    }
                snackbar.setDuration(3000);
                snackbar.setTextColor(resources.getColor(R.color.black))
                // set the background tint color for the snackbar
                snackbar.setBackgroundTint(resources.getColor(R.color.white));
                // set the action button text color of the snackbar however this is optional
                // as all the snackbar wont have the action button
                snackbar.setActionTextColor(resources.getColor(android.R.color.holo_red_dark));
                snackbar.show();
                onStart()
            }


        })

        binding.learnRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.learnRecyclerView.adapter = adapter

    }

    private fun getLearnBlogs() {

        //our API Interface
        val call: Call<ArrayList<LearnBlog>>? = RetrofitClient.instance?.myApi?.getHeroes()

        call?.enqueue(object : Callback<ArrayList<LearnBlog>> {


            override fun onResponse(
                call: Call<ArrayList<LearnBlog>>,
                response: Response<ArrayList<LearnBlog>>
            ) {
                learnBlogList = response.body() as List<LearnBlog>
                Log.d("blog", learnBlogList.toString())
                adapter.setData(learnBlogList)
            }

            override fun onFailure(call: Call<ArrayList<LearnBlog>>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("blog", t.message.toString())
            }
        })
    }


}