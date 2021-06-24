package com.shivtej.androidprojects.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.databinding.ActivityMainBinding
import com.shivtej.androidprojects.models.User
import java.sql.Time

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    lateinit var user: User


    companion object {

        const val morning = 0
        const val afternoon = 12
        const val evening = 16
        const val night = 20
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.navhostFragment)
        binding.bottomNavBar.setupWithNavController(navController)

        auth = FirebaseAuth.getInstance()

        //if previously login
        val user = auth.currentUser
        if (user != null) {
            navController.navigate(R.id.action_loginFragment_to_projectFragment)
            getUser(user.uid)
        }


        toolbarText()

    }

    fun getUser(uid: String) {
        val reference = Firebase.firestore.collection("User").document(uid)
        reference.get()
            .addOnSuccessListener {
                if (it != null) {
                    user = it.toObject<User>()!!
                    Log.i("user", it.data.toString())

                } else {
                    Log.i("user", "error: ")
                }
            }

    }

    fun hideView() {
        binding.bottomNavBar.visibility = View.GONE
        binding.toolbar.visibility = View.GONE
    }

    fun showView() {
        binding.bottomNavBar.visibility = View.VISIBLE
        binding.toolbar.visibility = View.VISIBLE
    }

    fun projectView() {
        binding.bottomNavBar.visibility = View.GONE
        binding.toolbar.visibility = View.VISIBLE
    }

    private fun toolbarText() {


        val time = Time(System.currentTimeMillis()).hours

        binding.toolbarTextView.text = if (time in (morning + 1) until afternoon) {
            Log.d("time", getString(R.string.good_morning))
            getString(R.string.good_morning)
        } else if (time in (afternoon + 1) until evening) {
            Log.d("time", getString(R.string.good_afternoon))
            getString(R.string.good_afternoon)
        } else if (time in (evening + 1) until night) {
            Log.d("time", getString(R.string.good_evening))
            getString(R.string.good_evening)
        } else {
            Log.d("time", getString(R.string.good_night))
            getString(R.string.good_afternoon)
        }

        Log.i("cal123", time.toString())

    }

    override fun onResume() {
        super.onResume()
        toolbarText()
    }


}