package com.example.ninjacarscalculator.calculations

import android.util.Log
import androidx.paging.LOGGER
import com.example.ninjacarscalculator.database.AllParametrs
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.floor

class CalcClass : Calc {
    override fun customsClearance(params: AllParametrs): Int {
        params.customsClearance = when (params.carPrice) {
            in 700001..1800000 -> 3100
            in 300001..700000 -> 1550
            in 0..300000 -> 775
            else -> 8530
        }
        return params.customsClearance
    }

    override fun util(params: AllParametrs): Int {
        val sdf = SimpleDateFormat("yyyy")
        val yearCAlendar = sdf.format(Date()).toInt()

        when (yearCAlendar - params.dateManufact) {
            in 0..2 -> params.util = 3400
            else -> params.util = 5200
        }
        return params.util
    }

    override fun fob2(params: AllParametrs): Int {
        params.FOB2 =
            (params.FOB + params.fastBid + params.transfertCar + params.nego + params.newCustomer + (params.buttonComission))
        return params.FOB2
    }

    override fun japanMoney(params: AllParametrs): Int {
        params.FOB2 =fob2(params)
        params.japanMoney = params.freightVL + params.FOB2 + params.carPrice
        return params.japanMoney
    }

    override fun rusMoney(params: AllParametrs): Int {
        params.brokerageServicesRegistration = brokerageServicesRegistration(params)
        params.customsFee = customsFee(params).fee
        var u =  params.customsFee + params. brokerageServicesRegistration + params.addServices + params.deliveryСity



        params.rusMoney =  (floor(u*1)/1).toInt()
            //(params.finalPrice - params.japanMoney* params.yen - params.japanMoney * params.yen * params.banksСommission / 100).toInt()
        return params.rusMoney
    }

    override fun brokerageServicesRegistration(params: AllParametrs): Int {
        params.brokerageServicesRegistration =
            params.sbkts + params.svh + params.lab + params.transfertTK + params.broker +
                    params.glonas + params.registr + params.myFee

        return params.brokerageServicesRegistration
    }

    override fun customsFee(params: AllParametrs): ForMyFee {
        params.customsClearance = customsClearance(params)
        params.util = util(params)
        var price = params.carPrice.toDouble() * params.yen / params.euro
        params.customsFee = (params.util + params.customsClearance + customsFeeFun(
            params.dateManufact,
            (floor (price*10)/10).toInt(),
            params.engineCapacity
        ).fee * (floor(params.euro*10)/10))
        var text = customsFeeFun(
            params.dateManufact,
            (floor (price*1)/1).toInt(),
            params.engineCapacity
        ).text
        return ForMyFee(text,params.customsFee)
    }

    override fun finalPrice(params: AllParametrs): Int {


        params.japanMoney = japanMoney(params)
        var mm = params.japanMoney*params.yen+ params.japanMoney*params.yen*params.banksСommission/100
        params.rusMoney = rusMoney(params)

        Log.d("dcfdvdfvdfvdf", "${(floor(mm*10)/10)}")

        params.finalPrice = params.rusMoney + (floor(mm*10)/10).toInt()
        return params.finalPrice
    }

fun defoarams():AllParametrs{
    return AllParametrs(
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
}
    fun jj(params: AllParametrs):AllParametrs{
       params.customsClearance =  customsClearance(params)
        params.util = util(params)
        params.FOB2 =   fob2(params)
        params.japanMoney =    japanMoney(params)
        params.rusMoney  =   rusMoney(params)
        params.brokerageServicesRegistration  =  brokerageServicesRegistration(params)
        params.customsFee =   customsFee(params).fee
        params.finalPrice  = finalPrice(params)

        return params
    }



    private fun customsFeeFun(year: Int, price: Int, value: Int): ForMyFee {
       var myFee = ForMyFee("",0.0)
        val sdf = SimpleDateFormat("yyyy")
        val currentDate = sdf.format(Date()).toInt()
        myFee = when (currentDate - year) {
            in 0..3 -> {
                lastPrice(price, value)
            }

            in 3..5 -> {
                lastPriceOldCar(value)
            }

            else -> {
                lastPriceOlderCar(value)
            }
        }
        return myFee
    }




    private fun lastPriceOlderCar(value: Int): ForMyFee {
        var cf = 0.0
        var text = ""
        when (value) {
            in 0..1000 -> {
                cf = value * 3.0
                text = "ставка 3.0€ за 1 куб.см"
            }

            in 1000..1500 -> {
                cf = value * 3.2
                text = "ставка 3.2€ за 1 куб.см"
            }

            in 1500..1800 -> {
                cf = value * 3.5
                text = "ставка 3.5€ за 1 куб.см"
            }

            in 1800..2300 -> {
                cf = value * 4.8
                text = "ставка 4.8€ за 1 куб.см"
            }

            in 2300..3000 -> {
                cf = value * 5.0
                text = "ставка 5.0€ за 1 куб.см"
            }

            else -> {
                cf = value * 5.7
                text = "ставка 5.7€ за 1 куб.см"
            }
        }
        return ForMyFee(text,cf)
    }


    private fun lastPriceOldCar(value: Int): ForMyFee {
        var cf = 0.0
        var text = ""
        when (value) {
            in 0..1000 -> {
                cf = value * 1.5
                text = "ставка 1.5€ за 1 куб.см"
            }

            in 1000..1500 -> {
                cf = value * 1.7
                text = "ставка 1.7€ за 1 куб.см"
            }

            in 1500..1800 -> {
                cf = value * 2.5
                text = "ставка 2.5€ за 1 куб.см"
            }

            in 1800..2300 -> {
                cf = value * 2.7
                text = "ставка 2.7€ за 1 куб.см"
            }

            in 2300..3000 -> {
                cf = value * 3.0
                text = "ставка 3.0€ за 1 куб.см"
            }

            else -> {
                cf = value * 3.6
                text = "ставка 3.6€ за 1 куб.см"
            }
        }
        return ForMyFee(text,cf)
    }


    private fun getlastCustomFee(price: Int, value: Int, proc: Int, coef: Double): ForMyFee {

        val summ: Double = price.toDouble() * proc.toDouble() / 100
        var cf = 0.0
        var text = ""
        if (summ / value < coef) {
            cf = summ
            text = "ставка $proc%"
        } else {
            cf = coef * value
            text = "ставка $coef€ за 1 куб.см"
        }
        return ForMyFee(text,cf)
    }
    private fun lastPrice(price: Int, value: Int): ForMyFee {
        var cf = 0.0
        var text=""
        when (price) {
            in 0..8500 -> {
                cf = getlastCustomFee(price, value, 54, 2.5).fee
                text = getlastCustomFee(price, value, 54, 2.5).text
            }

            in 8500..16700 -> {
                cf = getlastCustomFee(price, value, 48, 3.5).fee
                text = getlastCustomFee(price, value, 54, 2.5).text
            }

            in 16700..42300 -> {
                cf = getlastCustomFee(price, value, 48, 5.5).fee
                text = getlastCustomFee(price, value, 54, 2.5).text
            }

            in 42300..84500 -> {
                cf = getlastCustomFee(price, value, 48, 7.5).fee
                text = getlastCustomFee(price, value, 54, 2.5).text
            }

            in 84500..169000 -> {
                cf = getlastCustomFee(price, value, 48, 15.0).fee
                text = getlastCustomFee(price, value, 54, 2.5).text
            }

            else -> {
                cf = getlastCustomFee(price, value, 48, 20.0).fee
                text = getlastCustomFee(price, value, 54, 2.5).text
            }
        }
        return ForMyFee(text,cf)

    }




}

class ForMyFee(
    val text:String,
    val fee:Double
){


}