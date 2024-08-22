package com.example.ninjacarscalculator.ui.history

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ninjacarscalculator.R
import com.example.ninjacarscalculator.databinding.FragmentNotificationsBinding
import com.example.ninjacarscalculator.models.Task2
import com.example.ninjacarscalculator.models.UserData
import com.example.ninjacarscalculator.ui.main.MyDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NotificationsFragment : Fragment() {
    private val PREFERENCES_COUNTER_NAME = "settings"
    private val USER_ACCEPTED_NAME = "name"
    private val USER_ACCEPTED_NUMB = "numb"
    private lateinit var prefs: SharedPreferences
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val adapter = AdapterAutoName { Task2 -> onItemCkick(Task2) }
    val user = FirebaseAuth.getInstance().currentUser
    var dbReference = FirebaseDatabase.getInstance().getReference("users")
    private lateinit var taskMap: MutableList<Map<String, Any>>
    var userData: UserData? = null
    var saveAutoname: MutableList<String> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)
        prefs =
            requireContext().getSharedPreferences(PREFERENCES_COUNTER_NAME, Context.MODE_PRIVATE)

        notificationsViewModel.params.onEach {
            adapter.setData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.recycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.recycler.adapter = adapter

        val name = prefs.getString(USER_ACCEPTED_NAME, "User")
        val numb = prefs.getString(USER_ACCEPTED_NUMB, "Numb")
        binding.clear.setOnClickListener {
            val myDialogFragment = MyDialogFragment(
                "Вы действительно желаете очистить журнал сохраненных расчетов?",
                name!!,
                numb!!
            )
            val manager = requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            myDialogFragment.show(transaction, "dialog")

        }
        binding.bb.setOnClickListener {
            val url = "https://auc.aleado.com/auctions/?p=project/searchform&searchtype=max&s&ld"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }

    fun onItemCkick(item: Task2) {

        val bundle = Bundle()
        bundle.putString("nameAuto", item.nameAuto)

        findNavController().navigate(
            R.id.action_navigation_notifications_to_carInfoFragment,
            bundle
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}