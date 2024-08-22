package com.example.ninjacarscalculator.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//параметры для локального хранения данныx
@Entity(tableName = "parametrs")
data class AllParametrs(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "dateManufact")
    var dateManufact: Int,
    @ColumnInfo(name = "engineCapacity")
    var engineCapacity: Int,
    @ColumnInfo(name = "carPrice")
    var carPrice: Int,
    @ColumnInfo(name = "freightVL")
    var freightVL: Int,
    @ColumnInfo(name = "FOB")
    var FOB: Int,
    @ColumnInfo(name = "customsFee")
    var customsFee: Double,
    @ColumnInfo(name = "brokerageServicesRegistration")
    var brokerageServicesRegistration: Int,
    @ColumnInfo(name = "customsСoefficient")
    var customsСoefficient: Int,
    @ColumnInfo(name = "finalPrice")
    var finalPrice: Int,
    @ColumnInfo(name = "banksСommission")
    var banksСommission: Double,
    @ColumnInfo(name = "buttonComission")
    var buttonComission: Int,
    @ColumnInfo(name = "deliveryСity")
    var deliveryСity: Int,
//@ColumnInfo(name = "FOB")
//var FOB: Int,
    @ColumnInfo(name = "addServices")
    var addServices: Int,
    @ColumnInfo(name = "rusMoney")
    var rusMoney: Int,
    @ColumnInfo(name = "japanMoney")
    var japanMoney: Int,
//@ColumnInfo(name = "freightVL")
//var freightVL: Int,
    @ColumnInfo(name = "FOB2")
    var FOB2: Int,
    @ColumnInfo(name = "fastBid")
    var fastBid: Int,
    @ColumnInfo(name = "transfertCar")
    var transfertCar: Int,
    @ColumnInfo(name = "nego")
    var nego: Int,
    @ColumnInfo(name = "newCustomer")
    var newCustomer: Int,
    @ColumnInfo(name = "util")
    var util: Int,
//@ColumnInfo(name = "dateManufact")
//var dateManufact: Int,
    @ColumnInfo(name = "customsClearance")
    var customsClearance: Int,
    @ColumnInfo(name = "sbkts")
    var sbkts: Int,
    @ColumnInfo(name = "svh")
    var svh: Int,
    @ColumnInfo(name = "lab")
    var lab: Int,
    @ColumnInfo(name = "transfertTK")
    var transfertTK: Int,
    @ColumnInfo(name = "broker")
    var broker: Int,
    @ColumnInfo(name = "glonas")
    var glonas: Int,
    @ColumnInfo(name = "registr")
    var registr: Int,
    @ColumnInfo(name = "myFee")
    var myFee: Int,
    @ColumnInfo(name = "euro")
    var euro: Double,
    @ColumnInfo(name = "usd")
    var usd: Double,
    @ColumnInfo(name = "yen")
    var yen: Double,
) {

}
