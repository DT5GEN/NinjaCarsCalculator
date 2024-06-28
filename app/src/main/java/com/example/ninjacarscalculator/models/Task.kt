package com.example.ninjacarscalculator.models

import com.example.ninjacarscalculator.database.AllParametrs


data class Task(
 val date:String,
var nameAuto: String,
var params : AllParametrs
){

}
data class Task2(
    val date:String,
    var nameAuto: String,
    var params : Params
){

}
