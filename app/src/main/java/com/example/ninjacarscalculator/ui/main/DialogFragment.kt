package com.example.ninjacarscalculator.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.ninjacarscalculator.R
import com.example.ninjacarscalculator.database.AllParametrs
import com.example.ninjacarscalculator.models.Task
import com.example.ninjacarscalculator.models.UserData
import java.text.SimpleDateFormat
import java.util.Date

class MyDialogFragment(var title: String, var numb: String, var name: String) : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setMessage(title)
                .setPositiveButton("ОК") { dialog, id ->
                    dialog.cancel()
                    if (title == "Сохранять авто в журнал могут зарегестрированные пользователи") {
                        findNavController().navigate(R.id.action_navigation_dashboard_to_loginFragment)
                    } else {
                        val saveAutoname: MutableList<Task> = mutableListOf()
                        val params: AllParametrs = AllParametrs(
                            1,
                            2021,
                            660,
                            730000,
                            60000,
                            60000,
                            0.0,
                            0,
                            0,
                            0,
                            0.0,
                            20000,
                            0,
                            0,
                            0,
                            0,
                            60000,
                            0,
                            0,
                            0,
                            0,
                            5200,
                            3100,
                            20000,
                            30000,
                            5000,
                            0,
                            10000,
                            0,
                            0,
                            25000,
                            100.0,
                            90.0,
                            0.6
                        )
                        val user = FirebaseAuth.getInstance().currentUser
                        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                        val currentDate = sdf.format(Date()).toString()
                        val dbReference = FirebaseDatabase.getInstance().getReference("users")
                        saveAutoname += Task(currentDate, "default_car", params)
                        user?.run {
                            val userData = UserData(
                                numb = numb,
                                name = name,
                                saveAutoname = saveAutoname
                            )

                            dbReference.child(user.uid).setValue(userData)
                            findNavController().navigate(R.id.action_navigation_notifications_to_navigation_dashboard)
                        }
                    }
                }
                .setNegativeButton("Отмена") { dialogInterface, i -> }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}