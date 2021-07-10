package com.shivtej.androidprojects.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.adapters.LearnAdapter
import com.shivtej.androidprojects.adapters.OnClicked
import com.shivtej.androidprojects.databinding.FragmentLearnBinding
import com.shivtej.androidprojects.models.LearnBlog
import com.shivtej.androidprojects.ui.MainActivity
import com.shivtej.androidprojects.utils.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LearnFragment: Fragment(), OnClicked {

    private lateinit var binding: FragmentLearnBinding
    private lateinit var activity1: MainActivity

    private lateinit var learnBlogList: List<LearnBlog>

    private lateinit var navController: NavController


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

//        viewModel.getLearnBlog().observe(viewLifecycleOwner, Observer {
//            learnBlogList = it
//            adapter = LearnAdapter(learnBlogList, this)
//            binding.learnRecyclerView.adapter = adapter
//            adapter.notifyDataSetChanged()
//
//        })

        getLearnBlogs()

    }

    private fun getLearnBlogs() {

        //our API Interface
        //our API Interface
        val call: Call<ArrayList<LearnBlog>>? = RetrofitClient.instance?.myApi?.getHeroes()

        call?.enqueue(object : Callback<ArrayList<LearnBlog>> {


            override fun onResponse(
                call: Call<ArrayList<LearnBlog>>,
                response: Response<ArrayList<LearnBlog>>
            ) {
                val heroList: List<LearnBlog> = response.body() as List<LearnBlog>
                Log.d("blog", heroList.toString())
                setUpRecyclerView(heroList)
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

    private fun setUpRecyclerView(heroList: List<LearnBlog>) {
        val adapter = LearnAdapter(heroList, object : OnClicked{
            override fun onLearnBlogClicked(currentItem: LearnBlog) {
               val bundle = Bundle()
                bundle.putSerializable("blog", currentItem)
                navController.navigate(R.id.action_learnFragment_to_blogViewFragment, bundle)
            }
        })

        binding.learnRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.learnRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onLearnBlogClicked(currentItem: LearnBlog) {
        val bundle = Bundle()
        bundle.putSerializable("blog", currentItem)
        navController.navigate(R.id.action_learnFragment_to_blogViewFragment, bundle)
    }
}