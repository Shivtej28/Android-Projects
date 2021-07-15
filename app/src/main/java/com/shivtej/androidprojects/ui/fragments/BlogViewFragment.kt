package com.shivtej.androidprojects.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
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

class BlogViewFragment : Fragment() {

    private lateinit var binding: FragmentBlogViewBinding
    private lateinit var activity1: MainActivity
    private lateinit var blog: LearnBlog

    private val viewModel: SavedPostViewModel by activityViewModels()

    private lateinit var roomPostList: List<LearnBlog>

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
         blog = arguments?.getSerializable("blog") as LearnBlog

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
            if (roomPostList.contains(blog)) {

                //binding.bookmarkPost.setMinAndMaxProgress(0.0f, 0.5f)

//                binding.bookmarkPost.playAnimation()
                viewModel.deletePost(blog)
                binding.bookmarkPost.setMinAndMaxProgress(0.0f, 0.5f) //white

                val snackbar = Snackbar.make(binding.root, "Deleted Blog", Snackbar.LENGTH_SHORT)
                    .setAction("UNDO") {
                        viewModel.addPost(blog)

                        binding.bookmarkPost.setMinAndMaxProgress(0.5f, 1.0f)
                    }

                snackbar.setDuration(3000);
                snackbar.setTextColor(resources.getColor(R.color.black))
                // set the background tint color for the snackbar
                snackbar.setBackgroundTint(resources.getColor(R.color.white));
                // set the action button text color of the snackbar however this is optional
                // as all the snackbar wont have the action button
                snackbar.setActionTextColor(resources.getColor(android.R.color.holo_red_dark));
                snackbar.show();
            } else {
//                binding.bookmarkPost.setMinAndMaxProgress(0.0f, 0.5f)
//                binding.bookmarkPost.playAnimation()
                viewModel.addPost(blog)
                binding.bookmarkPost.setMinAndMaxProgress(0.5f, 1.0f)
                val snackbar = Snackbar.make(binding.root, "Saved Blog", Snackbar.LENGTH_SHORT)
                snackbar.setDuration(3000);
                snackbar.setTextColor(resources.getColor(R.color.black))
                // set the background tint color for the snackbar
                snackbar.setBackgroundTint(resources.getColor(R.color.white));
                // set the action button text color of the snackbar however this is optional
                // as all the snackbar wont have the action button
                snackbar.show();

            }

        }



        Log.d("blog", roomPostList.toString())
    }

    fun checkBlogInRoom(){
        roomPostList = viewModel.readAllPosts.value!!
        if (roomPostList.contains(blog)) {
            binding.bookmarkPost.setMinAndMaxProgress(0.5f, 1.0f) //black
        } else {
            binding.bookmarkPost.setMinAndMaxProgress(0.0f, 0.5f) //white

        }
    }


}

private class MyBrowser : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return true
    }
}