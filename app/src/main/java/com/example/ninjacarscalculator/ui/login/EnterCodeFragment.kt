package com.example.ninjacarscalculator.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ninjacarscalculator.MainActivity
import com.example.ninjacarscalculator.R
import com.example.ninjacarscalculator.databinding.FragmentEnterCodeBinding
import com.google.firebase.auth.PhoneAuthProvider

var ids = ""

class EnterCodeFragment : Fragment() {
    private lateinit var mPhoneNumber: String
    private var _binding: FragmentEnterCodeBinding? = null
    private val binding get() = _binding!!
    var name = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentEnterCodeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPhoneNumber = arguments?.getString("mPhoneNumber").toString()
        ids = arguments?.getString("id").toString()
        name = arguments?.getString("name").toString()
        binding.number.text = mPhoneNumber
        (activity as MainActivity).title = mPhoneNumber
        binding.registerInputCode.addTextChangedListener(AppTextWatcher {
            val string = binding.registerInputCode.text.toString()
            if (string.length == 6) {
                enterCode()
            }
        })
    }

    private fun enterCode() {
        val code = binding.registerInputCode.text.toString()
        val j = PhoneAuthProvider.getCredential(ids, code)
        mAuth.signInWithCredential(j).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val fragment = arguments?.getString("fragment").toString()
                Toast.makeText(
                    requireContext(),
                    "Добро пожаловать",
                    Toast.LENGTH_SHORT
                )
                    .show()
                val bundle = Bundle()
                bundle.putString("mPhoneNumber", mPhoneNumber)
                bundle.putString("name", name)
                if (fragment == "settings") {
                    findNavController().navigate(
                        R.id.action_enterCodeFragment_to_advancedSettingsFragment,
                        bundle
                    )
                } else {
                    findNavController().navigate(
                        R.id.action_enterCodeFragment_to_navigation_dashboard,
                        bundle
                    )
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    task.exception?.message.toString(),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
