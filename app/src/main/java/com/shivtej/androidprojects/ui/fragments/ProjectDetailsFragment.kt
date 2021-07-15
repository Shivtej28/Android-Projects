package com.shivtej.androidprojects.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.adapters.SliderAdapter
import com.shivtej.androidprojects.databinding.FragmentProjectDetailsBinding
import com.shivtej.androidprojects.models.Project
import com.shivtej.androidprojects.ui.MainActivity
import com.shivtej.androidprojects.utils.Constants
import com.smarteist.autoimageslider.SliderView

class ProjectDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProjectDetailsBinding
    private lateinit var project: Project
    private lateinit var activity1: MainActivity
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProjectDetailsBinding.inflate(inflater, container, false)
        MobileAds.initialize(requireContext())
        loadAd()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity1 = activity as MainActivity
        activity1.projectView()
        activity1.checkNetwork()
        project = arguments?.getSerializable("project") as Project
        Log.e("project", project.toString())



        val imagesList = getImagesList()
        activity1.hideView()
        setUpRecyclerView(imagesList)
        Log.d("link", imagesList.toString())
        val detailUrl = project.description
        binding.webView.loadUrl(detailUrl)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding.toolbarTextView.text = project.title

        binding.sourceCodeBtn.setOnClickListener {
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(activity)
            } else {
                goToCustomTab()
                requestNewInterstitial()
            }
        }

    }

    private fun loadAd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            requireContext(),
            Constants.testInterstitialId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("ProjectDetails", adError.message)
                    mInterstitialAd = null
                    goToCustomTab()
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d("ProjectDetails", "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                    mInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Log.d("ProjectDetails", "Ad was dismissed.")
                                goToCustomTab()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                                Log.d("ProjectDetails", "Ad failed to show.")
                                goToCustomTab()
                            }

                            override fun onAdShowedFullScreenContent() {
                                Log.d("ProjectDetails", "Ad showed fullscreen content.")
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

    private fun goToCustomTab() {
        val builder = CustomTabsIntent.Builder()
        val colorSchemeParams = CustomTabColorSchemeParams.Builder()
            .setNavigationBarColor(ContextCompat.getColor(requireContext(), R.color.androidbg))
            .setToolbarColor(ContextCompat.getColor(requireContext(), R.color.orange))
            .setSecondaryToolbarColor(ContextCompat.getColor(requireContext(), R.color.primary))
            .build()
        builder.setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_DARK, colorSchemeParams)
        val customTabIntent = builder.build()

        customTabIntent.launchUrl(requireContext(), Uri.parse(project.zipfile))

    }

    private fun setUpRecyclerView(imagesList: ArrayList<String>) {
        binding.imageRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = SliderAdapter(imagesList)
        binding.imageRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

    }

    private fun getImagesList(): ArrayList<String> {
        val imagesList: ArrayList<String> = ArrayList()
        if (project.ss1 != "") {
            imagesList.add(project.ss1)
        }
        if (project.ss2 != "") {
            imagesList.add(project.ss2)
        }
        if (project.ss3 != "") {
            imagesList.add(project.ss3)
        }
        if (project.ss4 != "") {
            imagesList.add(project.ss4)
        }
        if (project.ss5 != "") {
            imagesList.add(project.ss5)
        }
        if (project.ss6 != "") {
            imagesList.add(project.ss1)
        }
        if (project.ss7 != "") {
            imagesList.add(project.ss7)
        }
        if (project.ss8 != "") {
            imagesList.add(project.ss8)
        }
        if (project.ss9 != "") {
            imagesList.add(project.ss9)
        }
        return imagesList
    }
}