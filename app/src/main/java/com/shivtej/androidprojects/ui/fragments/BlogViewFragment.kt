package com.shivtej.androidprojects.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.shivtej.androidprojects.databinding.FragmentBlogViewBinding
import com.shivtej.androidprojects.models.LearnBlog
import com.shivtej.androidprojects.ui.MainActivity


class BlogViewFragment : Fragment() {

    private lateinit var binding: FragmentBlogViewBinding
    private lateinit var activity1 : MainActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBlogViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity1 = activity as MainActivity
        activity1.hideView()
        val blog = arguments?.getSerializable("blog") as LearnBlog

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding.blogWebView.webViewClient = MyBrowser()
        binding.blogWebView.webChromeClient = WebChromeClient()

        binding.blogWebView.loadUrl(blog.url.toString())

    }

}


private class MyBrowser : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return true
    }
}