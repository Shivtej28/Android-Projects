package com.shivtej.androidprojects.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.databinding.ActivityMainBinding
import com.shivtej.androidprojects.models.LearnBlog
import com.shivtej.androidprojects.models.User
import com.shivtej.androidprojects.utils.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Time
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

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

        navController = findNavController(R.id.navhostFragment)
        binding.bottomNavBar.setupWithNavController(navController)

        auth = FirebaseAuth.getInstance()

        //if previously login
        val user = auth.currentUser
        if (user != null) {
            navController.navigate(R.id.action_loginFragment_to_projectFragment)
            getUser(user.uid)
        }

        getRandomText()

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


    private fun getRandomText() {
        val randomToolbarText =
            arrayOf("Hi,", "Hello,", "Namaste,", "Hola,", "Hey")
        val randomValue = Random.nextInt(randomToolbarText.size)

        binding.toolbarTextView.text = randomToolbarText[randomValue]
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