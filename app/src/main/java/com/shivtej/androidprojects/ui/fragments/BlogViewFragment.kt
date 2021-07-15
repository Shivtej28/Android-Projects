package com.shivtej.androidprojects.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.gms.ads.*
import com.google.android.material.snackbar.Snackbar
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.databinding.FragmentBlogViewBinding
import com.shivtej.androidprojects.models.LearnBlog
import com.shivtej.androidprojects.ui.MainActivity
import com.shivtej.androidprojects.utils.Constants
import com.shivtej.androidprojects.viewModels.SavedPostViewModel
import kotlin.properties.Delegates

class BlogViewFragment : Fragment() {

    private lateinit var binding: FragmentBlogViewBinding
    private lateinit var activity1: MainActivity
    private lateinit var blog: LearnBlog

    private val viewModel: SavedPostViewModel by activityViewModels()

    //private lateinit var roomPostList: List<LearnBlog>

    private var isSaved by Delegates.notNull<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBlogViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity1 = activity as MainActivity
        activity1.hideView()
        activity1.checkNetwork()
        val args = BlogViewFragmentArgs.fromBundle(requireArguments())
        blog = args.blog
        checkBlogInRoom()

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        val adView = AdView(requireContext())

        adView.adSize = AdSize.FULL_BANNER

        adView.adUnitId = Constants.testBannerAd
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        binding.adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }

        binding.blogWebView.webViewClient = MyBrowser()
        binding.blogWebView.webChromeClient = WebChromeClient()

        binding.blogWebView.loadUrl(blog.url.toString())


        binding.bookmarkPost.setOnClickListener {
            if (isSaved) {
                viewModel.deletePost(blog)
                isSaved = false
                setBookmark()
                showDeleteSnackBar()

            } else {
                viewModel.addPost(blog)
                isSaved = true
                setBookmark()
                showSavedSnackBar()


            }


        }


//
    }

    private fun showSavedSnackBar() {
        val snackbar = Snackbar.make(binding.root, "Saved Blog", Snackbar.LENGTH_SHORT)
        snackbar.duration = 3000
        snackbar.setTextColor(resources.getColor(R.color.black))
        // set the background tint color for the snackbar
        snackbar.setBackgroundTint(resources.getColor(R.color.white))
        val sv = snackbar.view
        val lp = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        val height = binding.adView.height*2
        val h = binding.root.height
        lp.setMargins(0, h-height, 0, height)
        sv.layoutParams = lp
        Log.i("margin", h.toString())
        Log.i("margin", height.toString())

        snackbar.show()


    }

    private fun showDeleteSnackBar() {
        val snackbar = Snackbar.make(binding.root, "Deleted Blog", Snackbar.LENGTH_SHORT)
            .setAction("UNDO") {
                viewModel.addPost(blog)
                isSaved = true
                setBookmark()
            }

        snackbar.duration = 3000
        snackbar.setTextColor(resources.getColor(R.color.black))
        // set the background tint color for the snackbar
        snackbar.setBackgroundTint(resources.getColor(R.color.white))
        // set the action button text color of the snackbar however this is optional
        // as all the snackbar wont have the action button
        snackbar.setActionTextColor(resources.getColor(android.R.color.holo_red_dark))
        val sv = snackbar.view
        val lp = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        val height = binding.adView.height*2
        val h = binding.root.height
        lp.setMargins(0, h-height, 0, 0)

        sv.layoutParams = lp
        Log.i("margin", h.toString())
        Log.i("margin", height.toString())
        snackbar.show()
    }

    private fun setBookmark() {
        if (isSaved) {

            binding.bookmarkPost.setMinAndMaxProgress(0.0f, 0.5f)  //white
            binding.bookmarkPost.playAnimation()
            Log.d("isSaved", isSaved.toString())
        } else {
            binding.bookmarkPost.setMinAndMaxProgress(0.5f, 1.0f) //black
            binding.bookmarkPost.playAnimation()
            Log.d("isSaved", isSaved.toString())
        }


    }

    private fun checkBlogInRoom() {
        val roomPostList = viewModel.readAllPosts.value!!

        Log.d("blog", roomPostList.toString())
        if (roomPostList.contains(blog)) {
            isSaved = true
            binding.bookmarkPost.setMinAndMaxProgress(0.5f, 1.0f) //black
            Log.d("blog", "black")
        } else {
            isSaved = false
            binding.bookmarkPost.setMinAndMaxProgress(0.0f, 0.5f) //white
            Log.d("blog", "white")

        }
    }


}

private class MyBrowser : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return true
    }


}