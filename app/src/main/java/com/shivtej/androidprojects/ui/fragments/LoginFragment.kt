package com.shivtej.androidprojects.ui.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.databinding.FragmentLoginBinding
import com.shivtej.androidprojects.models.User
import com.shivtej.androidprojects.ui.MainActivity
import com.shivtej.androidprojects.utils.Constants
import com.shivtej.androidprojects.viewModels.ProjectViewModel
import java.sql.Time

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var activity1: MainActivity

    private val viewModel: ProjectViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarText()
        navController = Navigation.findNavController(view)
        activity1 = activity as MainActivity
        auth = FirebaseAuth.getInstance()
        //activity1.hideView()
        //if previously login
        val user = auth.currentUser
        //updateUI(user)

        binding.loginBtn.setOnClickListener {
            login()
        }

        binding.googleSignInBtn.setOnClickListener {
            signIn()
        }

        binding.createAccount.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity?.applicationContext!!, gso)

        binding.forgotPassword.setOnClickListener {
            resetPasswordDialog(it)
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int, data: Intent?
    ) {
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
                Log.w(TAG_GOOGLE, e.toString())
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
                    if (user != null) {
                        addUserToFirebase(user)
                    }
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG_GOOGLE, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun login() {

        if (!Patterns.EMAIL_ADDRESS.matcher(
                binding.emailEt.text.toString()
            ).matches()
        ) {
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
            activity1.getUser(user.uid)
        }
    }

    private fun addUserToFirebase(user: FirebaseUser) {
        val uid = user.uid
        val name = user.displayName
        val email = user.email
        val user = User(uid, name, email, 0)
        viewModel.addUserToFirebase(user)
    }

    private fun resetPasswordDialog(it: View) {
        val builder =
            Dialog(
                requireContext(),
                R.style.Base_ThemeOverlay_MaterialComponents_Dialog_Alert
            )
        builder.setContentView(R.layout.password_reset_dialog)
        builder.show()
        builder.setCanceledOnTouchOutside(false)

        val email: TextInputEditText = builder.findViewById(R.id.forgot_password_email)
        val reset: MaterialButton = builder.findViewById(R.id.reset_btn)

        val handler = Handler()

        reset.setOnClickListener {
            Firebase.auth.sendPasswordResetEmail(email.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Successfully sent mail to ${email.text} ✔️",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            val runnable = Runnable {
                if (builder.isShowing) {
                    builder.dismiss()
                }
            }

            builder.setOnDismissListener {
                handler.removeCallbacks(
                    runnable
                )
            }
            handler.postDelayed(runnable, 2000)
        }
    }

    private fun toolbarText() {

        val time = Time(System.currentTimeMillis()).hours

        binding.hello.text = if (time in (MainActivity.morning + 1) until MainActivity.afternoon) {
            Log.d("time", getString(R.string.good_morning))
            getString(R.string.good_morning)
        } else if (time in (MainActivity.afternoon + 1) until MainActivity.evening) {
            Log.d("time", getString(R.string.good_afternoon))
            getString(R.string.good_afternoon)
        } else {
            Log.d("time", getString(R.string.good_evening))
            getString(R.string.good_evening)
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
        private const val TAG_GOOGLE = "GoogleActivity"
    }
}