package com.shivtej.androidprojects

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import com.google.android.material.appbar.MaterialToolbar
import com.leo.simplearcloader.SimpleArcLoader
import com.shivtej.androidprojects.utils.Constants


class WebViewActivity : AppCompatActivity() {

    private var url = ""
    private var apptitle = ""
    private lateinit var webView: WebView
    private lateinit var archBar: SimpleArcLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        url = intent.getStringExtra(Constants.SOURCE_CODE_URL).toString()
        apptitle = intent.getStringExtra(Constants.TITLE).toString()
        webView = findViewById(R.id.webView)
        archBar = findViewById(R.id.progressBar)
        val toolbar: MaterialToolbar = findViewById(R.id.materialToolbar3)
        toolbar.title = apptitle
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        initializeWebView(url)


    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeWebView(url: String) {
        archBar.start()
        webView.settings.loadsImagesAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.settings.allowUniversalAccessFromFileURLs = true
        webView.settings.allowFileAccess = true
        webView.loadUrl(url)
        archBar.stop()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.webview_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.shareMenu -> {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
                var shareMessage = "Hey Check Out this Cool Android Project $apptitle \n $url \n"
                shareMessage =
                    "$shareMessage \n Download this app to get More Projects \n https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "choose one"))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}