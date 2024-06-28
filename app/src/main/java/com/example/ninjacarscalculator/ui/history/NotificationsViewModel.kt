package com.example.ninjacarscalculator.ui.history

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.example.ninjacarscalculator.models.Params
import com.example.ninjacarscalculator.models.Task2
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.reflect.KClass

class NotificationsViewModel : ViewModel() {
    val user = FirebaseAuth.getInstance().currentUser
    var dbReference = FirebaseDatabase.getInstance().getReference("users")

    private val _params = MutableStateFlow<List<Task2>>(emptyList())
    val params = _params.asStateFlow()

    init {
        addUserChangeListener()
    }



    private fun addUserChangeListener() {
        try {
            user?.run {
                val userIdReference = Firebase.database.reference
                    .child("users").child(uid).child("saveAutoname")
                userIdReference.get().addOnSuccessListener { it ->
                    var  taskMapd = it.value as MutableList<Map<String, Any>>
                  val g = emptyList<String>().toMutableList()
                    val l = emptyList<Params>().toMutableList()
                    val h = emptyList<Task2>().toMutableList()

                    taskMapd.forEach {
                        if (it["nameAuto"].toString() != "default_car") {
                            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                            val currentDate = sdf.format(Date()).toString()
                            g += it["nameAuto"].toString()
                            var j = (it["params"] as Map<String, Any>).toMutableMap()
                            j["customsFee"] =  j["customsFee"].toString().toDouble()
                            j["banksСommission"] =  j["banksСommission"].toString().toDouble()
                            j["euro"] =  j["euro"].toString().toDouble()
                            j["usd"] =  j["usd"].toString().toDouble()
                            j["yen"] =  j["yen"].toString().toDouble()


                            l += mapToObject(j, Params::class)

                         h += Task2(
                             currentDate,
                         it["nameAuto"].toString(),
                         mapToObject(j, Params::class)
                        )

                        }

                    }
                    _params.value = h
                }
            }
        }catch (_: NumberFormatException) {
            IllegalArgumentException("IllegalArgumentException")
        }
    }
    fun <T : Any> mapToObject(map: Map<String, Any>, clazz: KClass<T>): T {
        val constructor = clazz.constructors.first()
        val args = constructor
            .parameters
            .map { it to map.get(it.name) }
            .toMap()
        return constructor.callBy(args)
    }

}