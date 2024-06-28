package com.example.ninjacarscalculator.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import com.example.ninjacarscalculator.database.AllParametrs
import com.example.ninjacarscalculator.database.Dao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val teamDao: Dao) : ViewModel() {

    fun addDefaultParams(
        id: Int,
        dateManufact: Int,
        engineCapacity: Int,
        carPrice: Int,
        freightVL: Int,
        FOB: Int,
        customsFee: Double,
        brokerageServicesRegistration: Int,
        customsСoefficient: Int,
        finalPrice: Int,
        banksСommission: Double,
        buttonComission: Int,
        deliveryСity: Int,
        addServices: Int,
        rusMoney: Int,
        japanMoney: Int,
        FOB2: Int,
        fastBid: Int,
        transfertCar: Int,
        nego: Int,
        newCustomer: Int,
        util: Int,
        customsClearance: Int,
        sbkts: Int,
        svh : Int,
        lab : Int,
        transfertTK: Int,
        broker : Int,
        glonas : Int,
        registr : Int,
        myFee : Int,
        euro: Double,
        usd: Double,
        yen: Double,
    ) {
        viewModelScope.launch {
            teamDao.insert(
                AllParametrs(
                    id,
                    dateManufact,
                    engineCapacity,
                    carPrice,
                    freightVL,
                    FOB,
                    customsFee,
                    brokerageServicesRegistration,
                    customsСoefficient,
                    finalPrice,
                    banksСommission,
                    buttonComission,
                    deliveryСity,
                    addServices,
                    rusMoney,
                    japanMoney,
                    FOB2,
                    fastBid,
                    transfertCar,
                    nego,
                    newCustomer,
                    util,
                    customsClearance,
                    sbkts,
                    svh ,
                    lab,
                    transfertTK,
                    broker ,
                    glonas ,
                    registr ,
                    myFee,
                    euro,
                    usd,
                    yen
                    )
            )

        }

    }

}