package com.shivtej.androidprojects.ads

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.shivtej.androidprojects.utils.Constants

class ShowAd {

    lateinit var rewardedAd: RewardedAd
    lateinit var mInterstitialAd : InterstitialAd

    fun loadRewardedAd(context: Context){
        rewardedAd = RewardedAd(context, Constants.mRewardAdUnitId)
        rewardedAd.loadAd(AdRequest.Builder().build(), object : RewardedAdLoadCallback(){
            override fun onRewardedAdFailedToLoad(p0: Int) {
                super.onRewardedAdFailedToLoad(p0)
                loadRewardedAd(context)
            }

        })
    }

    fun loadInterstitialAd(context: Context){
        mInterstitialAd = InterstitialAd(context)
        MobileAds.initialize(context, Constants.mAPPUnitId)
        mInterstitialAd.adUnitId = Constants.mInterstitialAdUnitId
        mInterstitialAd.loadAd(AdRequest.Builder().build())
    }
}