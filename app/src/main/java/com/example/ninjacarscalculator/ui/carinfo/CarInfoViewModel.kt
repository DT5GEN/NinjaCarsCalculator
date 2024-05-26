package com.example.ninjacarscalculator.ui.carinfo

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.example.ninjacarscalculator.models.Params
import com.example.ninjacarscalculator.models.Task2
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlin.reflect.KClass

class CarInfoViewModel : ViewModel() {
    val user = FirebaseAuth.getInstance().currentUser
    var dbReference = FirebaseDatabase.getInstance().getReference("users")

    private val _params = MutableStateFlow<List<Task2>>(emptyList())
    val params = _params.asStateFlow()


    var listParams =   emptyList<CarInfoModel>().toMutableList()

fun forParamList(){
   params.onEach {
       if (it.isNotEmpty()) {
           with(it[0].params) {
               listParams.add(CarInfoModel("Дата производства ", "$dateManufact"))
               listParams.add(CarInfoModel("Объём двигателя", "$engineCapacity"))
               listParams.add(CarInfoModel("цена покупки на аукционе", "$carPrice"))
               listParams.add(CarInfoModel("Фрахт до Владивостока", "$freightVL"))
               listParams.add(CarInfoModel("FOB расходы по Японии", "$FOB"))
               listParams.add(CarInfoModel("Таможенная пошлина", "$customsFee"))
               listParams.add(
                   CarInfoModel(
                       "Услуги брокера и оформление",
                       "$brokerageServicesRegistration"
                   )
               )
               listParams.add(CarInfoModel("Таможенная ставка", "$customsСoefficient"))
               listParams.add(CarInfoModel("итоговая цена в России", "$finalPrice"))
               listParams.add(CarInfoModel("Комиссия вашего банка", "$banksСommission"))
               listParams.add(CarInfoModel("комиссия за кнопку", "$buttonComission"))
               listParams.add(CarInfoModel("Доставка в другой город", "$deliveryСity"))
               listParams.add(CarInfoModel("Дополнительные услуги", "$addServices"))
               listParams.add(CarInfoModel("Затраты в России ", "$rusMoney"))
               listParams.add(CarInfoModel("Затраты в Японии", "$japanMoney"))
               listParams.add(CarInfoModel("FOB2", "$FOB2"))
               listParams.add(CarInfoModel("Ставка менее чем за час", "$fastBid"))
               listParams.add(CarInfoModel("Перегрузка на др. Стоянку", "$transfertCar"))
               listParams.add(CarInfoModel("Покупка через переговоры", "$nego"))
               listParams.add(CarInfoModel("Единичный заказ (новый клиент) ", "$newCustomer"))
               listParams.add(CarInfoModel("Утилизационный сбор", "$util"))
               listParams.add(CarInfoModel("Таможенное оформление", "$customsClearance"))
               listParams.add(CarInfoModel("СБКТС", "$sbkts"))
               listParams.add(CarInfoModel("СВХ", "$svh"))
               listParams.add(CarInfoModel("Лаборатария + стоянка", "$lab"))
               listParams.add(CarInfoModel("Перегон до ТК", "$transfertTK"))
               listParams.add(CarInfoModel("Услуги брокера", "$broker"))
               listParams.add(CarInfoModel("ГЛОНАС", "$glonas"))
               listParams.add(CarInfoModel("Временная регистрация", "$registr"))
               listParams.add(CarInfoModel("комиссия за подбор авто", "$myFee"))
           }

       }
   }
}




     fun addUserChangeListener(autoNameS:String) {
      try {
        user?.run {
            val userIdReference = Firebase.database.reference
                .child("users").child(uid).child("saveAutoname")
            userIdReference.get().addOnSuccessListener { it ->
                var  taskMapd = it.value as MutableList<Map<String, Any>>
                val h = emptyList<Task2>().toMutableList()
                     taskMapd.forEach {
                    if (it["nameAuto"].toString() == autoNameS) {

                        var j = (it["params"] as Map<String, Any>).toMutableMap()
                        j["customsFee"] =  j["customsFee"].toString().toDouble()
                        j["banksСommission"] =  j["banksСommission"].toString().toDouble()
                        j["euro"] =  j["euro"].toString().toDouble()
                        j["usd"] =  j["usd"].toString().toDouble()
                        j["yen"] =  j["yen"].toString().toDouble()


                        h += Task2(
                            it["date"].toString(),
                            it["nameAuto"].toString(),
                            mapToObject(j as Map<String, Any>, Params::class)
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