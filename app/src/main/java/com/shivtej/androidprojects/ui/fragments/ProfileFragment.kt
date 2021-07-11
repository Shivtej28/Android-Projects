package com.shivtej.androidprojects.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.databinding.FragmentProfileBinding
import com.shivtej.androidprojects.models.Order
import com.shivtej.androidprojects.models.PaymentInterface
import com.shivtej.androidprojects.models.User
import com.shivtej.androidprojects.ui.MainActivity
import com.shivtej.androidprojects.viewModels.ProjectViewModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileFragment : Fragment(), PaymentResultWithDataListener {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var activity1: MainActivity
    lateinit var manager: ReviewManager
    var reviewInfo: ReviewInfo? = null
    private lateinit var navController: NavController
    var user: User? = null
    private val viewModel: ProjectViewModel by activityViewModels()
    private lateinit var retrofit: Retrofit
    private lateinit var retroInterface: PaymentInterface

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

        Checkout.preload(context)

        if (user == null) {

            user = activity1.user


        }
        setData()

        initReviews()

        retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.43.156:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retroInterface = retrofit.create(PaymentInterface::class.java)

        binding.donateCardView.setOnClickListener {
            binding.paymentLayout.visibility = View.VISIBLE
        }

        binding.payBtn.setOnClickListener {

            val amount = binding.amountEditText.text.toString()

            if (amount.isEmpty()) {
                return@setOnClickListener
            }

            getOrderId(amount)
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

        binding.todoDoneCardView.setOnClickListener {
            navController.navigate(R.id.action_profileFragment_to_savedPostFragment)
        }
    }

    private fun getOrderId(amount: String) {

        val map = HashMap<String, String>()
        map["amount"] = amount

        retroInterface.getOrderId(map).enqueue(object : Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.body() != null) {
                    initiatePayment(amount, response.body()!!)
                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun initiatePayment(amount: String, order: Order) {

        val checkout = Checkout()
        checkout.setKeyID(order.getKeyId())

        checkout.setImage(R.drawable.logowithoutname)

        val paymentOptions = JSONObject()
        paymentOptions.put("Name", "Android Projects and Quizzes")
        paymentOptions.put("amount", amount)
        paymentOptions.put("order_id", order.getOrderId())
        paymentOptions.put("currency", "INR")
        paymentOptions.put("description", "Thanks You for supporting us!!")

        checkout.open(activity1, paymentOptions)

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

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {

        val map = HashMap<String, String>()
        map["order_id"] = p1!!.orderId
        map["pay_id"] = p1.paymentId
        map["signature"] = p1.signature

        retroInterface.updateTransaction(map).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.body().equals("success")) {
                    Toast.makeText(context, "Payment Successful", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(context, "Payment Failed", Toast.LENGTH_LONG).show()
    }
}