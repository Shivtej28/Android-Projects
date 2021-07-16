package com.shivtej.androidprojects.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.adapters.LearnAdapter
import com.shivtej.androidprojects.adapters.OnClicked
import com.shivtej.androidprojects.databinding.FragmentLearnBinding
import com.shivtej.androidprojects.models.LearnBlog
import com.shivtej.androidprojects.ui.MainActivity
import com.shivtej.androidprojects.utils.Constants
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
    private val TAG = "LearnFragment"

    private var mInterstitialAd: InterstitialAd? = null

    private lateinit var current: LearnBlog


    private val viewModel: SavedPostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLearnBinding.inflate(inflater, container, false)
        MobileAds.initialize(requireContext())
        loadAd()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity1 = activity as MainActivity
        activity1.showView()
        activity1.checkNetwork()
        navController = Navigation.findNavController(view)

        learnBlogList = emptyList()


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
                current = currentItem
                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(requireActivity())
                } else {
                    goToBlogView()
                    requestNewInterstitial()
                }
            }

            override fun savePost(currentItem: LearnBlog) {
                viewModel.addPost(currentItem)
                showSavedSnackBar()

                onStart()
            }

            override fun deletePost(currentItem: LearnBlog) {
                current = currentItem
                viewModel.deletePost(currentItem)

                showDeleteSnackBar()
                onStart()
            }


        })

        binding.learnRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.learnRecyclerView.adapter = adapter

    }

    private fun goToBlogView() {

        navController.navigate(LearnFragmentDirections.actionLearnFragmentToBlogViewFragment(current))
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

    private fun loadAd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            requireContext(),
            Constants.testInterstitialId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                    mInterstitialAd = null

                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                    mInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Log.d(TAG, "Ad was dismissed.")
                                goToBlogView()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                                Log.d(TAG, "Ad failed to show.")
                                goToBlogView()
                            }

                            override fun onAdShowedFullScreenContent() {
                                Log.d(TAG, "Ad showed fullscreen content.")
                                mInterstitialAd = null
                            }
                        }
                }
            })
    }

    private fun requestNewInterstitial() {
        if (mInterstitialAd == null) {
            loadAd()
        }
    }

    private fun showSavedSnackBar() {
        val snackbar = Snackbar.make(binding.root, "Saved Blog", Snackbar.LENGTH_SHORT)
        snackbar.duration = 3000
        snackbar.setTextColor(resources.getColor(R.color.black))
        // set the background tint color for the snackbar
        snackbar.setBackgroundTint(resources.getColor(R.color.light))
        val sv = snackbar.view
        val lp = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        val height = activity1.binding.bottomNavBar.height
        val h = binding.root.height
        lp.setMargins(0, h, 0, height)
        sv.layoutParams = lp
        Log.i("margin", h.toString())
        Log.i("margin", height.toString())

        snackbar.show()


    }

    private fun showDeleteSnackBar() {
        val snackbar = Snackbar.make(binding.root, "Deleted Blog", Snackbar.LENGTH_SHORT)
            .setAction("UNDO") {
                viewModel.addPost(current)
                onStart()
            }

        snackbar.duration = 3000
        snackbar.setTextColor(resources.getColor(R.color.black))
        // set the background tint color for the snackbar
        snackbar.setBackgroundTint(resources.getColor(R.color.light))
        // set the action button text color of the snackbar however this is optional
        // as all the snackbar wont have the action button
        snackbar.setActionTextColor(resources.getColor(android.R.color.holo_red_dark))
        val sv = snackbar.view
        val lp = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        val height = activity1.binding.bottomNavBar.height
        val h = binding.root.height
        lp.setMargins(0, h, 0, 0)

        sv.layoutParams = lp
        Log.i("margin", h.toString())
        Log.i("margin", height.toString())
        snackbar.show()
    }


}