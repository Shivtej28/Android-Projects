package com.shivtej.androidprojects.ui.fragments

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.databinding.FragmentSignUpBinding
import com.shivtej.androidprojects.models.User
import com.shivtej.androidprojects.ui.MainActivity
import com.shivtej.androidprojects.viewModels.ProjectViewModel

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var activity1: MainActivity
    private lateinit var userName: String
    private lateinit var userEmail: String

    private val viewModel: ProjectViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        activity1 = activity as MainActivity
        activity1.hideView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        auth = FirebaseAuth.getInstance()

        binding.createAccount.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser() {
        if (binding.nameEt.text.toString().isEmpty()) {
            binding.nameEt.error = "Please enter Name"
            binding.nameEt.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEt.text.toString()).matches()) {
            binding.emailEt.error = "Please enter valid email"
            binding.emailEt.requestFocus()
            return
        }
        if (binding.passwordEt.text.toString().isEmpty()) {
            binding.passwordEt.error = "Please enter Password"
            binding.passwordEt.requestFocus()
            return
        }

        userName = binding.nameEt.text.toString()
        userEmail = binding.emailEt.text.toString()

        auth.createUserWithEmailAndPassword(
            userEmail,
            binding.passwordEt.text.toString()
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            addUserToFirebase(user)
            navController.navigate(R.id.action_signUpFragment_to_projectFragment)
        }

    }

    private fun addUserToFirebase(user: FirebaseUser) {
        val uid = user.uid
        val user = User(uid, userName, userEmail, 0)
        viewModel.addUserToFirebase(user)

    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}