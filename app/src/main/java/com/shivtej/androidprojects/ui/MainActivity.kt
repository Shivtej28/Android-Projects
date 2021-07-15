package com.shivtej.androidprojects.ui

import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
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
import kotlin.random.Random

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
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
            getUser(user.uid)
            navController.navigate(R.id.action_loginFragment_to_projectFragment)

        }
    }

        fun checkNetwork() {
            if (!checkInternetConnection()) {
                showNetworkErrorDialogBox()
            }
        }

        private fun showNetworkErrorDialogBox() {
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setTitle(getString(R.string.alertTitle))
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
            alertDialog.setMessage(getString(R.string.internet_not_connected))
            alertDialog.setButton(
                DialogInterface.BUTTON_NEUTRAL, "OK"
            ) { dialog, which ->
                alertDialog.dismiss()
            }
            alertDialog.show()
        }

        fun checkInternetConnection(): Boolean {
            val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            return activeNetwork?.isConnectedOrConnecting == true
        }

        fun getUser(uid: String) {
            val reference = Firebase.firestore.collection("User").document(uid)
            reference.get()
                .addOnSuccessListener {
                    if (it != null) {
                        user = it.toObject<User>()!!
                        getRandomText()
                        Log.i("user", it.data.toString())

                    } else {
                        Log.i("user", "error: ")
                    }
                }
        }


        private fun getRandomText() {
            val randomToolbarText =
                arrayOf("Hi,", "Hello,", "Namaste,\uD83D\uDE4F", "Hola,", "Hey,")
            val randomValue = Random.nextInt(randomToolbarText.size)
            val name = user.userName?.split(" ")
            Log.i("name", name?.get(0).toString())
            val n = name?.get(0)
            (randomToolbarText[randomValue] + n).also { binding.toolbarTextView.text = it }
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