package com.shivtej.androidprojects

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.shivtej.androidprojects.adapters.SSRvAdapter
import com.shivtej.androidprojects.ads.ShowAd
import com.shivtej.androidprojects.databinding.ActivityProjectBinding
import com.shivtej.androidprojects.models.Project
import com.shivtej.androidprojects.utils.Constants
import java.io.File


class ProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectBinding
    private lateinit var project: Project
    private lateinit var imageUrlList: ArrayList<String>
    private lateinit var adapter: SSRvAdapter
    private var downloadId: Long = 0
    private lateinit var snackbar: Snackbar
    private lateinit var apptitle: String
    lateinit var appname: String
    private lateinit var dm: DownloadManager
    private lateinit var apk: String
    private lateinit var zipfile : String
    lateinit var mInterstitialAd : InterstitialAd
    lateinit var mInterstitialad2 : InterstitialAd



    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.getBundleExtra(Constants.BUNDLE)
        project = bundle?.get(Constants.PROJECT) as Project

      //  projectshowAd.loadInterstitialAd(this)
        loadAd()
        loadAd2()

        binding.tvDescription.loadUrl(project.description)

        mInterstitialAd.adListener = object : AdListener(){
            override fun onAdClicked() {
                super.onAdClicked()
                mInterstitialAd.adListener.onAdClosed()
            }

            override fun onAdClosed() {
                super.onAdClosed()
                Log.i("Closed", "Closed")
                gotoWebView()

            }

        }
        mInterstitialad2.adListener = object : AdListener(){
            override fun onAdClicked() {
                super.onAdClicked()
                mInterstitialad2.adListener.onAdClosed()
            }

            override fun onAdClosed() {
                super.onAdClosed()
                Log.i("Closed", "Closed")
                checkPermissions()

            }

        }

        Log.i("Project", project.toString())
        apptitle = project.title
        val description = project.description
        apk = project.apk
        zipfile = project.zipfile

        imageUrlList = ArrayList()
        adapter = SSRvAdapter(imageUrlList)
        binding.recyclerView.adapter = adapter

        addUrlToList()

        binding.toolbar1.title = apptitle
        setSupportActionBar(binding.toolbar1)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)
        //binding.toolbar1.setNavigationIcon(R.drawable.ic_back_arrow)
        binding.toolbar1.setNavigationOnClickListener {
           onBackPressed()

        }



        binding.btnSourceCode.setOnClickListener {
            if(mInterstitialAd.isLoaded){
                mInterstitialAd.show()
            }else{
                gotoWebView()
            }
        }

//        binding.btnAPk.setOnClickListener {
//            snackbar = Snackbar.make(it, "Download Complete", Snackbar.LENGTH_LONG)
//            if (mInterstitialad2.isLoaded){
//                mInterstitialad2.show()
//            }else{
//                checkPermissions()
//            }
//
//        }

        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    snackbar.setActionTextColor(Color.BLUE)
                        .setAction("Open") {
                            val builder = VmPolicy.Builder()
                            StrictMode.setVmPolicy(builder.build())
                            val install = Intent(Intent.ACTION_OPEN_DOCUMENT)
                            install.setDataAndType(
                                Uri.fromFile(
                                    File(
                                        Environment.getExternalStorageDirectory().path,
                                        "/download/$appname.apk"
                                    )
                                ),
                                dm.getMimeTypeForDownloadedFile(id)
                            )
                            startActivity(Intent.createChooser(install, "Open File with"))
                        }
                        .setBackgroundTint(Color.BLACK)
                        .setTextColor(Color.WHITE)
                    snackbar.show()

                }
            }
        }
        registerReceiver(broadcastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    private fun loadAd2() {
        mInterstitialad2 = InterstitialAd(this)
        MobileAds.initialize(this, Constants.mAPPUnitId)
        mInterstitialad2.adUnitId = Constants.testInterstitialId
        mInterstitialad2.loadAd(AdRequest.Builder().build())
    }

    private fun loadAd() {

        mInterstitialAd = InterstitialAd(this)
        MobileAds.initialize(this, Constants.mAPPUnitId)
        mInterstitialAd.adUnitId = Constants.testInterstitialId
        mInterstitialAd.loadAd(AdRequest.Builder().build())
    }

//    private fun runadevent() {
//
//        showAd.mInterstitialAd.adListener = object : AdListener(){
//            override fun onAdClicked() {
//                super.onAdOpened()
//                showAd.mInterstitialAd.adListener.onAdClosed()
//            }
//
//            override fun onAdClosed() {
//                super.onAdClosed()
//                gotoWebView()
//                Log.i("Closed", "Closed")
//            }
//        }
//    }

    private fun gotoWebView() {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra(Constants.SOURCE_CODE_URL, zipfile)
        intent.putExtra(Constants.TITLE, apptitle)
        startActivity(intent)
    }

    @ExperimentalStdlibApi
    private fun checkPermissions() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        } else {
            downloadApk()
        }
    }

    @ExperimentalStdlibApi
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() || grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadApk()
                }
            }
        }
    }

    @ExperimentalStdlibApi
    fun downloadApk() {
        appname = apptitle.lowercase().replace(" ", "")
        val request = DownloadManager.Request(
            Uri.parse(apk)
        ).setTitle("Android Projects And Quizzes")
            .setDescription("$apptitle Downloading..")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "$appname.apk"
            )

            .setAllowedOverMetered(true)

        Toast.makeText(this, "Downloading Started..", Toast.LENGTH_SHORT).show()
        dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadId = dm.enqueue(request)

    }


    private fun addUrlToList() {
        val ss1 = project.ss1
        val ss2 = project.ss2
        val ss3 = project.ss3
        val ss4 = project.ss4
        val ss5 = project.ss5

        if (ss1 != "") {
            imageUrlList.add(ss1)
        }
        if (ss2 != "") {
            imageUrlList.add(ss2)
        }
        if (ss3 != "") {
            imageUrlList.add(ss3)
        }
        if (ss4 != "") {
            imageUrlList.add(ss4)
        }
        if (ss5 != "") {
            imageUrlList.add(ss4)
        }

        adapter.notifyDataSetChanged()


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }




}





