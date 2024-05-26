package com.example.ninjacarscalculator.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ninjacarscalculator.R
import com.example.ninjacarscalculator.database.App
import com.example.ninjacarscalculator.database.Dao
import com.example.ninjacarscalculator.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class HomeFragment : Fragment() {
    private val PREFERENCES_COUNTER_NAME = "settings"
    private val USER_ACCEPTED_LOGIN = "firstlogin"
    private val USER_ACCEPTED_SAVED = "saved"
    private lateinit var prefs: SharedPreferences
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val teamDaoVM: Dao = (requireActivity().application as App).db.myteamDao()
                return HomeViewModel(teamDaoVM) as T
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sdft = SimpleDateFormat("yyyy")
        var yearCar = sdft.format(Date()).toInt()-3
        prefs =
            requireContext().getSharedPreferences(PREFERENCES_COUNTER_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val firstLogin = prefs.getString(USER_ACCEPTED_LOGIN, "false")
//        editor.putString(USER_ACCEPTED_SAVED, "false").apply()
        if (firstLogin == "false") {
            homeViewModel.addDefaultParams(
                id = 1,
                dateManufact = yearCar,
                engineCapacity = 660,
                carPrice = 730000,
                freightVL = 60000,
                FOB=60000,
                customsFee = 0.0,
                brokerageServicesRegistration = 0,
                customsСoefficient = 0,
                finalPrice = 0,
                banksСommission = 0.0,
                buttonComission = 20000,
                deliveryСity = 0,
                addServices = 0,
                rusMoney = 0,
                japanMoney = 0,
                FOB2 = 60000,
                fastBid = 0,
                transfertCar = 0,
                nego = 0,
                newCustomer = 0,
                util = 5200,
                customsClearance = 3100,
                sbkts = 20000,
                svh = 30000,
                lab = 5000,
                transfertTK = 0,
                broker = 10000,
                glonas = 0,
                registr = 0,
                myFee = 25000,
                euro = 100.0,
                usd = 90.0,
                yen = 0.6)
            editor.putString(USER_ACCEPTED_LOGIN,"true").apply()
            lifecycleScope.launch {
                delay(1500)
                findNavController().navigate(R.id.action_navigation_home_to_navigation_dashboard)
            }
        } else {
            lifecycleScope.launch {
                delay(1500)
                findNavController().navigate(R.id.action_navigation_home_to_navigation_dashboard)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}