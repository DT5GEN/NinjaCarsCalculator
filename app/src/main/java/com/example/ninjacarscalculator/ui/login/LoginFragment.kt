package com.example.ninjacarscalculator.ui.login

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.example.ninjacarscalculator.MainActivity
import com.example.ninjacarscalculator.R
import com.example.ninjacarscalculator.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputEditText
import java.util.concurrent.TimeUnit

lateinit var mAuth: FirebaseAuth
class LoginFragment : Fragment() {
    private lateinit var mPhoneNumber:String
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
      //  mAuth.firebaseAuthSettings.setAppVerificationDisabledForTesting(false)



            mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(
                        requireContext(),
                        p0.message.toString(),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                    val bundle = Bundle()
                    var name = binding.username.editableText.toString()
                    bundle.putString("mPhoneNumber", mPhoneNumber)
                    bundle.putString("name", name)
                    bundle.putString("id", id)
                    findNavController().navigate(
                        R.id.action_loginFragment_to_enterCodeFragment,
                        bundle
                    )
                }

            }



            binding.login.setOnClickListener {
                if(isOnline(requireContext())) {
                binding.loading.isVisible = true
                sendCode()
            }else{
                    val text = "Проверте подключение к интернету"
                    val duration = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(requireContext(), text, duration)
                    toast.show()
                }


        }




    }
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
    private fun sendCode() {
        if (binding.registerInputPhoneNumber.text.toString().isEmpty()){
            Toast.makeText(requireContext(),
                "Введите номер",
                Toast.LENGTH_SHORT)
                .show()

        } else {
            authUser()
        }
    }

    private fun authUser() {
        mPhoneNumber = binding.registerInputPhoneNumber.text.toString()
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(mPhoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity as MainActivity)                 // Activity (for callback binding)
            .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}