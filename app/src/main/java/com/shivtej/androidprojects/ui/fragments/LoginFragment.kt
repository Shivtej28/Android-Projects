package com.shivtej.androidprojects.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.databinding.FragmentLoginBinding
import com.shivtej.androidprojects.ui.MainActivity
import com.shivtej.androidprojects.utils.Constants
import java.security.Policy

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        activity?.findViewById<MaterialToolbar>(R.id.toolbar)?.visibility = View.GONE
        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_bar)?.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        navController = Navigation.findNavController(view)

        auth = FirebaseAuth.getInstance()

        binding.loginBtn.setOnClickListener {
            login()
        }

        binding.googleSignInBtn.setOnClickListener {
            signIn()
        }

        binding.createAccount.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity?.applicationContext!!, gso)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Constants.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG_GOOGLE, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG_GOOGLE, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG_GOOGLE, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG_GOOGLE, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun login() {

        if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEt.text.toString()).matches()) {
            binding.emailEt.error = "Please enter valid email"
            binding.emailEt.requestFocus()
            return
        }
        if (binding.passwordEt.text.toString().isEmpty()) {
            binding.passwordEt.error = "Please enter password"
            binding.passwordEt.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(
            binding.emailEt.text.toString(),
            binding.passwordEt.text.toString()
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            navController.navigate(R.id.action_loginFragment_to_projectFragment)
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
        private const val TAG_GOOGLE = "GoogleActivity"
    }
}