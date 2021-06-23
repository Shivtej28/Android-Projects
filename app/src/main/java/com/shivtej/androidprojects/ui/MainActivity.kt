package com.shivtej.androidprojects.ui


import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.databinding.ActivityMainBinding
import com.shivtej.androidprojects.models.User
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    lateinit var user: User


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
//        if (user != null) {
//            navController.navigate(R.id.action_loginFragment_to_projectFragment)
//        }

        //getUser(auth.uid.toString())

        toolbarText()

    }

//    private fun getUser(uid: String) {
//        val reference = Firebase.firestore.collection("User").document(uid)
//        reference.get()
//            .addOnSuccessListener {
//                if (it != null) {
//                   // user = it.toObject<User>()!!
//                    //Log.i("user", user.toString())
//                    val name = user.userName.toString()
//
//                } else {
//                    Log.i("user", "error: ")
//                }
//            }
//
//    }

    fun hideView() {
        binding.bottomNavBar.visibility = View.GONE
        binding.toolbar.visibility = View.GONE
    }

    fun showView() {
        binding.bottomNavBar.visibility = View.VISIBLE
        binding.toolbar.visibility = View.VISIBLE
    }

    private fun toolbarText() {
        val currentTime: Date = Calendar.getInstance().time

        val string1 = "00:00:00"
        val morning = SimpleDateFormat("HH:mm:ss").parse(string1)
        val calendar1 = Calendar.getInstance()
        calendar1.time = morning
        calendar1.add(Calendar.DATE, 1)

        val string2 = "12:00:00"
        val noon = SimpleDateFormat("HH:mm:ss").parse(string2)
        val calendar2 = Calendar.getInstance()
        calendar2.time = noon
        calendar2.add(Calendar.DATE, 1)

        val string3 = "16:00:00"
        val evening = SimpleDateFormat("HH:mm:ss").parse(string3)
        val calendar3 = Calendar.getInstance()
        calendar3.time = evening
        calendar3.add(Calendar.DATE, 1)

        val string4 = "20:00:00"
        val night = SimpleDateFormat("HH:mm:ss").parse(string4)
        val calendar4 = Calendar.getInstance()
        calendar4.time = night
        calendar4.add(Calendar.DATE, 1)

        if (currentTime.after(calendar1.time) && currentTime.before(calendar2.time)) {
            binding.toolbarTextView.text = R.string.good_morning.toString()
        } else if (currentTime.after(calendar2.time) && currentTime.before(calendar3.time)) {
            binding.toolbarTextView.text = R.string.good_afternoon.toString()
        } else if (currentTime.after(calendar3.time) && currentTime.before(calendar4.time)) {
            binding.toolbarTextView.text = R.string.good_evening.toString()
        } else if (currentTime.after(calendar4.time) && currentTime.before(calendar1.time)) {
            binding.toolbarTextView.text = R.string.good_night.toString()
        }
    }
}