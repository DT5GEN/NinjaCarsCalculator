package com.example.ninjacarscalculator.ui.carinfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ninjacarscalculator.databinding.FragmentCarInfoBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CarInfoFragment : Fragment() {
    private var _binding: FragmentCarInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CarInfoViewModel
    private val adapter = CarInfoAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCarInfoBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CarInfoViewModel::class.java)
        val autoName = arguments?.getString("nameAuto").toString()

        viewModel.addUserChangeListener(autoName)
        val listParams = emptyList<CarInfoModel>().toMutableList()

        viewModel.params.onEach {
            if (it.isNotEmpty()) {
                binding.date.text = it[0].date
                binding.name.text = it[0].nameAuto
                with(it[0].params) {
                    if ("$dateManufact" != "0" && "$dateManufact" != "null") {
                        listParams.add(CarInfoModel("Дата производства ", "$dateManufact"))
                    }
                    if ("$engineCapacity" != "0" && "$engineCapacity" != "null") {
                        listParams.add(CarInfoModel("Объём двигателя", "$engineCapacity"))
                    }
                    if ("$carPrice" != "0" && "$carPrice" != "null") {
                        listParams.add(CarInfoModel("цена покупки на аукционе", "$carPrice"))
                    }
                    if ("$freightVL" != "0" && "$freightVL" != "null") {
                        listParams.add(CarInfoModel("Фрахт до Владивостока", "$freightVL"))
                    }
                    if ("$FOB" != "0" && "$FOB" != "null") {
                        listParams.add(CarInfoModel("FOB расходы по Японии", "$FOB"))
                    }
                    if ("$customsFee" != "0" && "$customsFee" != "null") {

                        listParams.add(CarInfoModel("Таможенная пошлина", "$customsFee"))
                    }
                    if ("$brokerageServicesRegistration" != "0" && "$brokerageServicesRegistration" != "null") {
                        listParams.add(
                            CarInfoModel(
                                "Услуги брокера и оформление",
                                "$brokerageServicesRegistration"
                            )
                        )
                    }
                    if ("$customsСoefficient" != "0" && "$customsСoefficient" != "null") {
                        listParams.add(CarInfoModel("Таможенная ставка", "$customsСoefficient"))
                    }
                    if ("$banksСommission" != "0" && "$banksСommission" != "null") {
                        listParams.add(CarInfoModel("Комиссия вашего банка", "$banksСommission"))
                    }
                    if ("$buttonComission" != "0" && "$buttonComission" != "null") {
                        listParams.add(CarInfoModel("комиссия за кнопку", "$buttonComission"))
                    }
                    if ("$deliveryСity" != "0" && "$deliveryСity" != "null") {
                        listParams.add(CarInfoModel("Доставка в другой город", "$deliveryСity"))
                    }
                    if ("$addServices" != "0" && "$addServices" != "null") {
                        listParams.add(CarInfoModel("Дополнительные услуги", "$addServices"))
                    }
                    if ("$rusMoney" != "0" && "$rusMoney" != "null") {
                        listParams.add(CarInfoModel("Затраты в России ", "$rusMoney"))
                    }
                    if ("$japanMoney" != "0" && "$japanMoney" != "null") {
                        listParams.add(CarInfoModel("Затраты в Японии", "$japanMoney"))
                    }
                    if ("$FOB2" != "0" && "$FOB2" != "null") {
                        listParams.add(CarInfoModel("FOB2", "$FOB2"))
                    }
                    if ("$fastBid" != "0" && "$fastBid" != "null") {
                        listParams.add(CarInfoModel("Ставка менее чем за час", "$fastBid"))
                    }
                    if ("$transfertCar" != "0" && "$transfertCar" != "null") {
                        listParams.add(CarInfoModel("Перегрузка на др. Стоянку", "$transfertCar"))
                    }
                    if ("$nego" != "0" && "$nego" != "null") {
                        listParams.add(CarInfoModel("Покупка через переговоры", "$nego"))
                    }
                    if ("$newCustomer" != "0" && "$newCustomer" != "null") {
                        listParams.add(
                            CarInfoModel(
                                "Единичный заказ (новый клиент) ",
                                "$newCustomer"
                            )
                        )
                    }
                    if ("$util" != "0" && "$util" != "null") {
                        listParams.add(CarInfoModel("Утилизационный сбор", "$util"))
                    }
                    if ("$customsClearance" != "0" && "$customsClearance" != "null") {
                        listParams.add(CarInfoModel("Таможенное оформление", "$customsClearance"))
                    }
                    if ("$sbkts" != "0" && "$sbkts" != "null") {
                        listParams.add(CarInfoModel("СБКТС", "$sbkts"))
                    }
                    if ("$svh" != "0" && "$svh" != "null") {
                        listParams.add(CarInfoModel("СВХ", "$svh"))
                    }
                    if ("$lab" != "0" && "$lab" != "null") {
                        listParams.add(CarInfoModel("Лаборатария + стоянка", "$lab"))
                    }
                    if ("$transfertTK" != "0" && "$transfertTK" != "null") {
                        listParams.add(CarInfoModel("Перегон до ТК", "$transfertTK"))
                    }
                    if ("$broker" != "0" && "$broker" != "null") {
                        listParams.add(CarInfoModel("Услуги брокера", "$broker"))
                    }
                    if ("$glonas" != "0" && "$glonas" != "null") {
                        listParams.add(CarInfoModel("ГЛОНАС", "$glonas"))
                    }
                    if ("$registr" != "0" && "$registr" != "null") {
                        listParams.add(CarInfoModel("Временная регистрация", "$registr"))
                    }
                    if ("$myFee" != "0" && "$myFee" != "null") {
                        listParams.add(CarInfoModel("комиссия за подбор авто", "$myFee"))
                    }
                    if ("$finalPrice" != "0" && "$finalPrice" != "null") {
                        listParams.add(CarInfoModel("итоговая цена в России", "$finalPrice"))
                    }
                }

                adapter.setData(listParams)
                binding.recycler.layoutManager =
                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                binding.recycler.adapter = adapter

            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.bottomButton.setOnClickListener {
            val url = "https://auc.aleado.com/auctions/?p=project/searchform&searchtype=max&s&ld"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}