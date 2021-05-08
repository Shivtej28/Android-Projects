package com.shivtej.androidprojects

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.BuildConfig
import com.shivtej.androidprojects.adapters.ItemClicked
import com.shivtej.androidprojects.adapters.ProjectsRVAdapter
import com.shivtej.androidprojects.ads.ShowAd
import com.shivtej.androidprojects.databinding.ActivityMainBinding
import com.shivtej.androidprojects.models.ListGradient
import com.shivtej.androidprojects.utils.Constants

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    val showAd = ShowAd()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showAd.loadRewardedAd(this)

        setSupportActionBar(binding.materialToolbar)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, binding.drawerLayout, R.string.nav_open, R.string.nav_close)

        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()



        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navview.setNavigationItemSelectedListener(this)
        actionBarDrawerToggle.drawerArrowDrawable.color = resources.getColor(R.color.navyblue)


        val list: ArrayList<ListGradient> = ArrayList()
        val listGradient1 = ListGradient("Basic", R.drawable.basicgradient)
        val listGradient2 = ListGradient("Intermediate", R.drawable.basicgradient)
        val listGradient3 = ListGradient("Advance", R.drawable.basicgradient)
        list.add(listGradient1)
        list.add(listGradient2)
        list.add(listGradient3)

        val quiz1 = ListGradient("Kotlin Quiz", R.drawable.basicgradient)
        val quiz2 = ListGradient("Android Quiz", R.drawable.basicgradient)

        val quizList: ArrayList<ListGradient> = ArrayList()
        quizList.add(quiz1)
        quizList.add(quiz2)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvProjects.layoutManager = linearLayoutManager
        val projectsRVAdapter = ProjectsRVAdapter(list, object : ItemClicked {
            override fun onItemClicked(string: ListGradient) {
                val intent = Intent(this@MainActivity, ProjectListActivity::class.java)
                intent.putExtra(Constants.PROJECT_LEVEL, string.item)

                startActivity(intent)
            }
        })
        binding.rvProjects.adapter = projectsRVAdapter

        val quizManager = LinearLayoutManager(this)
        quizManager.orientation = LinearLayoutManager.HORIZONTAL

        val quizAdapter = ProjectsRVAdapter(quizList, object : ItemClicked {
            override fun onItemClicked(string: ListGradient) {
                val intent = Intent(this@MainActivity, QuizActivity::class.java)
                intent.putExtra(Constants.QUIZ_NAME, string.item)
                startActivity(intent)
            }

        })

        binding.rvQuizzes.layoutManager = quizManager
        binding.rvQuizzes.adapter = quizAdapter


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                binding.drawerLayout.closeDrawers()
                return true
            }
            R.id.donate -> {
                //Toast.makeText(this, "Donate", Toast.LENGTH_SHORT).show()
                showVideoAd()
                return true
            }
            R.id.feedback -> {
                // Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show()
                sendEmail()
                return true
            }
            R.id.rate -> {
                //Toast.makeText(this, "Rate us", Toast.LENGTH_SHORT).show()
                rating()
                return true
            }
            R.id.share ->{
                shareApp()
                return true
            }
            R.id.check_for_updates -> {
                //Toast.makeText(this, "About", Toast.LENGTH_SHORT).show()
                checkForUpdates()
                return true
            }
        }
        return false
    }

    private fun checkForUpdates() {
        val url = "market://details?id=$packageName"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }

    private fun shareApp() {
        val appId = packageName
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
        var shareMessage = "Hey Checkout this Awesome App Here you will get some cool android projects to start " +
                "your android development journey Download App now \n\n"
        shareMessage =
            """
                ${shareMessage}https://play.google.com/store/apps/details?id=$appId
                
                
                 """.trimIndent()
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, "choose one"))
    }

    private fun showVideoAd() {

        if (showAd.rewardedAd.isLoaded) {
            showAd.rewardedAd.show(this, object : RewardedAdCallback() {
                override fun onUserEarnedReward(p0: RewardItem) {
                    super.onUserEarnedReward(p0)
                    Toast.makeText(this@MainActivity, "Thank You", Toast.LENGTH_SHORT).show()
                }
            })
        }
        showAd.loadRewardedAd(this)
    }

    private fun rating() {

        val builder = AlertDialog.Builder(this)

        builder.setTitle("Please Rate Us")
        builder.setIcon(R.drawable.ic_rate)
        builder.setMessage(
            "Thanks for using the application. If you like ${
                applicationInfo.loadLabel(
                    packageManager
                )
            } please rate us! Your feedback is important for us!"
        )
        builder.setPositiveButton("Rate it") { _: DialogInterface, i: Int ->
            val url = "market://details?id=$packageName"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url))
            startActivity(intent)

        }
        builder.setNegativeButton("No") { _: DialogInterface, i: Int -> }

        builder.show()
    }

    fun sendEmail() {
        val emailIntent = Intent(
            Intent.ACTION_SENDTO,
            Uri.fromParts("mailto", "asdevelopers1428@gmail.com", null)
        )
        startActivity(Intent.createChooser(emailIntent, "Contact Us"))
    }
}