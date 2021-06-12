package com.shivtej.androidprojects.ui


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.databinding.ActivityMainBinding
import com.shivtej.androidprojects.viewModels.ProjectViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.navhostFragment)
        binding.bottomNavBar.setupWithNavController(navController)

        auth = FirebaseAuth.getInstance()

        //if previously login
        val user = auth.currentUser
        if(user != null){
            navController.navigate(R.id.action_loginFragment_to_projectFragment)
        }

    }


     fun hideView(){
        binding.bottomNavBar.visibility = View.GONE
        binding.toolbar.visibility = View.GONE
    }

     fun showView(){
        binding.bottomNavBar.visibility = View.VISIBLE
        binding.toolbar.visibility = View.VISIBLE
    }

    

}