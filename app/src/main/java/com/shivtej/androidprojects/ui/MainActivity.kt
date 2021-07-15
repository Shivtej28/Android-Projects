package com.shivtej.androidprojects.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.databinding.ActivityMainBinding
import com.shivtej.androidprojects.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    lateinit var user: User
    val TAG = "MainActivity"

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

        navController = findNavController(R.id.navhostFragment)
        binding.bottomNavBar.setupWithNavController(navController)

        auth = FirebaseAuth.getInstance()

        //if previously login
        val user = auth.currentUser
        if (user != null) {
            navController.navigate(R.id.action_loginFragment_to_projectFragment)
            getUser(user.uid)
        }

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
}