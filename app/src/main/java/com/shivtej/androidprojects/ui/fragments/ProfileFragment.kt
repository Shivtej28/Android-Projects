package com.shivtej.androidprojects.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import com.google.android.play.core.review.testing.FakeReviewManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.databinding.FragmentProfileBinding
import com.shivtej.androidprojects.models.User
import com.shivtej.androidprojects.ui.MainActivity
import com.shivtej.androidprojects.viewModels.ProjectViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var activity1: MainActivity
    lateinit var manager: ReviewManager
    var reviewInfo: ReviewInfo? = null
    private lateinit var navController: NavController
    var user: User? = null
    private val viewModel: ProjectViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        activity1 = activity as MainActivity
        activity1.showView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        val auth = Firebase.auth

        if (user == null) {
            val uid = auth.currentUser?.uid
            user = activity1.user


        }
        setData()

        initReviews()

        binding.donateCardView.setOnClickListener {
            binding.paymentLayout.visibility = View.VISIBLE
        }

        binding.rateUsCardView.setOnClickListener {
            askForReview()

        }

        binding.feedbackCardView.setOnClickListener {
            val email = arrayOf("asdevelopers1428@gmail.com")

            val browserIntent = Intent(
                Intent.ACTION_SENDTO
            )
            browserIntent.setType("text/plain")
                .setData(
                    Uri.parse(
                        "mailto:"
                    )
                ).putExtra(Intent.EXTRA_EMAIL, email)
                .setPackage("com.google.android.gm")
            startActivity(Intent.createChooser(browserIntent, "Send Your Email"))
        }

        binding.shareCardView.setOnClickListener {
            ShareCompat.IntentBuilder.from(activity1)
                .setType("text/bold")
                .setChooserTitle("Chooser Title")
                .setText("Check out this amazing App at:\n https://play.google.com/store/apps/details?id=com.shivtej.androidprojects")
                .startChooser()
        }

        binding.updateCardView.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    "https://play.google.com/store/apps/details?id=com.shivtej.androidprojects"
                )
            )
            startActivity(browserIntent)
        }

        binding.logoutCardView.setOnClickListener {

            auth.signOut()
            activity1.hideView()
            navController.navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }

    private fun setData() {
        val name = user!!.userName.toString()
        val initial = name[0].toString()
        binding.nameInitial.text = initial
        binding.nameTextView.text = name
        binding.tvEmail.text = user!!.email.toString()
    }

    private fun initReviews() {
        manager = ReviewManagerFactory.create(requireContext())
        manager.requestReviewFlow().addOnCompleteListener { request ->
            if (request.isSuccessful) {
                reviewInfo = request.result
            } else {
                // Log error

                Log.i("error", "Error ")
            }
        }
    }

    private fun askForReview() {
        if (reviewInfo != null) {
            activity?.let {
                manager.launchReviewFlow(it, reviewInfo!!).addOnFailureListener {
                    // Log error and continue with the flow
                    Log.i("error", "Error ")

                }.addOnCompleteListener { _ ->
                    // Log success and continue with the flow
                    Log.i("error", "Success ")
                }
            }
        }
    }
}