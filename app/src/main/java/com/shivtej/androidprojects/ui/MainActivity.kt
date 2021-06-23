package com.shivtej.androidprojects.ui


import android.os.Bundle
import android.util.Log
import android.view.View
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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    lateinit var user1: User


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
        }

        getUser(auth.uid.toString())

    }

    private fun getUser(uid: String) {
        val reference = Firebase.firestore.collection("User").document(uid)
        reference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    val user1 = documentSnapshot.toObject(User::class.java)
                    Log.i("user", user1.toString())


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
}