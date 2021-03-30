package com.shivtej.androidprojects

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.shivtej.androidprojects.adapters.SSRvAdapter
import com.shivtej.androidprojects.databinding.ActivityProjectBinding
import com.shivtej.androidprojects.models.Project
import java.io.File


class ProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectBinding
    private lateinit var project: Project
    private lateinit var imageUrlList: ArrayList<String>
    private lateinit var adapter: SSRvAdapter
    private var downloadId: Long = 0
    private lateinit var snackbar: Snackbar
    lateinit var apptitle: String
    lateinit var appname: String
    private lateinit var dm: DownloadManager

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.getBundleExtra("bundle")
        project = bundle?.get("project") as Project

        Log.i("Project", project.toString())
        apptitle = project.title
        val description = project.description
        val apk = project.apk
        val zipfile = project.zipfile
        val image = project.image

        imageUrlList = ArrayList()
        adapter = SSRvAdapter(imageUrlList)
        binding.recyclerView.adapter = adapter

        addUrlToList()

        binding.toolbar1.title = apptitle
        binding.toolbar1.setNavigationIcon(R.drawable.ic_back_arrow)
        binding.toolbar1.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.tvDescription.text = description

        binding.btnSourceCode.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("url", zipfile)
            intent.putExtra("title", apptitle)
            startActivity(intent)
        }

        binding.btnAPk.setOnClickListener {
            snackbar = Snackbar.make(it, "Download Complete", Snackbar.LENGTH_LONG)
            appname = apptitle.lowercase().replace(" ", "")
            val request = DownloadManager.Request(
                Uri.parse(apk)
            ).setTitle("Android Projects And Quizzes")
                .setDescription("$apptitle Downloading..")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    appname + ".apk"
                )

                .setAllowedOverMetered(true)

            Toast.makeText(this, "Downloading Started..", Toast.LENGTH_SHORT).show()
            dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadId = dm.enqueue(request)

        }

        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                var id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    snackbar.setActionTextColor(Color.GREEN)
                        .setAction("Open", object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                val builder = VmPolicy.Builder()
                                StrictMode.setVmPolicy(builder.build())
                                val install = Intent(Intent.ACTION_OPEN_DOCUMENT)
                                install.setDataAndType(
                                    Uri.fromFile(
                                        File(
                                            Environment.getExternalStorageDirectory().path,
                                            "/download/" + appname + ".apk"
                                        )
                                    ),
                                    dm.getMimeTypeForDownloadedFile(id)
                                )
                                startActivity(Intent.createChooser(install, "Open File with"))
                            }
                        })
                        .setBackgroundTint(Color.WHITE)
                        .setTextColor(Color.BLACK)
                    snackbar.show()
                    // Toast.makeText(this@ProjectActivity, "Complete", Toast.LENGTH_SHORT).show()
                }
            }
        }
        registerReceiver(broadcastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    private fun addUrlToList() {
        val ss1 = project.ss1
        val ss2 = project.ss2
        val ss3 = project.ss3
        val ss4 = project.ss4
        val ss5 = project.ss5

        if (!ss1.equals("")) {
            imageUrlList.add(ss1)
        }
        if (!ss2.equals("")) {
            imageUrlList.add(ss2)
        }
        if (!ss3.equals("")) {
            imageUrlList.add(ss3)
        }
        if (!ss4.equals("")) {
            imageUrlList.add(ss4)
        }
        if (!ss5.equals("")) {
            imageUrlList.add(ss4)
        }

        adapter.notifyDataSetChanged()


    }


}





