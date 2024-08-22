package com.example.ninjacarscalculator.ui.advancedsettings

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ninjacarscalculator.R
import com.example.ninjacarscalculator.calculations.CalcClass
import com.example.ninjacarscalculator.database.AllParametrs
import com.example.ninjacarscalculator.databinding.FragmentAdvancedSettingsBinding
import com.example.ninjacarscalculator.ui.login.AppTextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

    //доп настройки
class AdvancedSettingsFragment : Fragment() {
    private var _binding: FragmentAdvancedSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AdvancedSettingsViewModel

    // lateinit var params: AllParametrs
    var FOB2 = 0
    private val USER_ACCEPTED_SAVED = "saved"
    private val USER_ACCEPTED_MON = "mon"
    private lateinit var prefs: SharedPreferences
    private val PREFERENCES_COUNTER_NAME = "settings"
    private val USER_ACCEPTED_NAME = "name"
    private val USER_ACCEPTED_NUMB = "numb"
    var params: AllParametrs = CalcClass().defoarams()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAdvancedSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(
                this,
                ViewModelFactoryAS(requireActivity())
            )[AdvancedSettingsViewModel::class.java]
        prefs =
            requireContext().getSharedPreferences(PREFERENCES_COUNTER_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val text = "Это поле расчитывается автоматически"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(requireContext(), text, duration)
        val firstName = prefs.getString(USER_ACCEPTED_NAME, "User")
        val firstNumb = prefs.getString(USER_ACCEPTED_NUMB, "Numb")
        var name = ""
        val saved = prefs.getString(USER_ACCEPTED_SAVED, "false")
        var numb = ""
        if (firstName == "User") {
            name = arguments?.getString("name").toString()
            numb = arguments?.getString("mPhoneNumber").toString()
            editor.putString(USER_ACCEPTED_NAME, name).apply()
            editor.putString(USER_ACCEPTED_NUMB, numb).apply()
        }



        viewModel.echangeRates.onEach {

            if (it != null) {
                params.euro = "${it.Valute.EUR.Value}".toDouble()
                params.usd = "${it.Valute.USD.Value}".toDouble()
                params.yen = "${it.Valute.JPY.Value}".toDouble()
            }

        }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.params.onEach {
            if (it.isNotEmpty()) {
                params = it.firstOrNull()!!


                params.finalPrice = CalcClass().finalPrice(params)

                binding.freight.setText("${params.freightVL}")
                binding.fob.setText("${params.FOB}")
                binding.util.setText("${params.util}")
                binding.myFee.setText("${params.myFee}")
                binding.registr.setText("${params.registr}")
                binding.glonas.setText("${params.glonas}")
                binding.broker.setText("${params.broker}")
                binding.transfertTK.setText("${params.transfertTK}")
                binding.lab.setText("${params.lab}")
                binding.svh.setText("${params.svh}")
                binding.sbkts.setText("${params.sbkts}")
                binding.newCustomer.setText("${params.newCustomer}")
                binding.nego.setText("${params.nego}")
                binding.transfertCar.setText("${params.transfertCar}")
                binding.fastBid.setText("${params.fastBid}")
                binding.customsClearance.setText("${params.customsClearance}")
                binding.japanMoney.setText("${params.japanMoney}")
                binding.rusMoney.setText("${params.rusMoney}")

            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)


        val animAlpha: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.alpha)

        binding.util.setOnClickListener {
            toast.show()
            binding.util.startAnimation(animAlpha)
        }

        binding.customsClearance.setOnClickListener {
            toast.show()
            binding.customsClearance.startAnimation(animAlpha)
        }

        binding.japanMoney.setOnClickListener {
            toast.show()
            binding.japanMoney.startAnimation(animAlpha)
        }
        binding.rusMoney.setOnClickListener {
            toast.show()
            binding.rusMoney.startAnimation(animAlpha)
        }



        binding.freight.setOnClickListener {
            val t1 = "Фрахт до Владивостока"
            val t2 = ""
            val t3 = "Цена"
            val b1 = binding.freight
            editTextPicker(
                t1,
                t2,
                t3,
                b1,
                5000,
                999999,
                "в текущее время цены на доставку около 60 000¥",
                ""
            )
        }
        binding.fob.setOnClickListener {
            val t1 = "FOB  (Расходы по Японии)"
            val t2 = ""
            val t3 = "Цена"
            val b1 = binding.fob
            //fob
            editTextPicker(
                t1,
                t2,
                t3,
                b1,
                5000,
                999999,
                "в текущее время FOB выходит около 60 000¥",
                ""
            )
        }

        binding.myFee.setOnClickListener {

            val t1 = "Комиссия за подбор авто "
            val t2 = ""
            val t3 = "Цена"
            val b1 = binding.myFee
            editTextPicker(
                t1,
                t2,
                t3,
                b1,
                1000,
                999999,
                "Значение не может быть меньше 1000",
                "Значение не может быть больше 999999",
            )
        }

        binding.registr.setOnClickListener {

            val t1 = " Временная регистрация"
            val t2 = ""
            val t3 = "Цена"
            val b1 = binding.registr
            editTextPicker(
                t1,
                t2,
                t3,
                b1,
                1000,
                999999,
                "Значение не может быть меньше 1000",
                "Значение не может быть больше 999999",
            )
        }
        binding.glonas.setOnClickListener {
            val t1 = "ГЛОНАС"
            val t2 = ""
            val t3 = "Цена"
            val b1 = binding.glonas
            editTextPicker(
                t1,
                t2,
                t3,
                b1,
                1000,
                999999,
                "Значение не может быть меньше 1000",
                "Значение не может быть больше 999999",
            )


        }
        binding.broker.setOnClickListener {

            val t1 = "Услуги брокера"
            val t2 = ""
            val t3 = "Цена"
            val b1 = binding.broker
            editTextPicker(
                t1,
                t2,
                t3,
                b1,
                1000,
                999999,
                "Значение не может быть меньше 1000",
                "Значение не может быть больше 999999",
            )

        }


        binding.transfertTK.setOnClickListener {
            val t1 = " Перегон до ТК"
            val t2 = ""
            val t3 = "Цена"
            val b1 = binding.transfertTK
            editTextPicker(
                t1,
                t2,
                t3,
                b1,
                1000,
                999999,
                "Значение не может быть меньше 1000",
                "Значение не может быть больше 999999",
            )
        }

        binding.lab.setOnClickListener {
            val t1 = "Лабаратория"
            val t2 = ""
            val t3 = "Цена"
            val b1 = binding.lab
            editTextPicker(
                t1,
                t2,
                t3,
                b1,
                1000,
                999999,
                "Значение не может быть меньше 1000",
                "Значение не может быть больше 999999",
            )
        }


        binding.svh.setOnClickListener {
            val t1 = "СВХ"
            val t2 = ""
            val t3 = "Цена"
            val b1 = binding.svh
            editTextPicker(
                t1,
                t2,
                t3,
                b1,
                1000,
                999999,
                "Значение не может быть меньше 1000",
                "Значение не может быть больше 999999",
            )
        }

        binding.sbkts.setOnClickListener {
            val t1 = "СБКТС"
            val t2 = ""
            val t3 = "Цена"
            val b1 = binding.sbkts
            editTextPicker(
                t1,
                t2,
                t3,
                b1,
                1000,
                999999,
                "Значение не может быть меньше 1000",
                "Значение не может быть больше 999999",
            )
        }


        binding.newCustomer.setOnClickListener {
            val t1 = "Единичный заказ"
            val t2 = ""
            val t3 = "Цена"
            val b1 = binding.newCustomer
            editTextPicker(
                t1,
                t2,
                t3,
                b1,
                100,
                99999,
                "Значение не может быть меньше 100",
                "Значение не может быть больше 99999",
            )
        }

        binding.nego.setOnClickListener {
            val t1 = "Покупка через переговоры"
            val t2 = ""
            val t3 = "Цена"
            val b1 = binding.nego
            editTextPicker(
                t1,
                t2,
                t3,
                b1,
                100,
                99999,
                "Значение не может быть меньше 100",
                "Значение не может быть больше 99999",
            )
        }

        binding.transfertCar.setOnClickListener {
            val t1 = "Перегрузка на др. Стоянку"
            val t2 = ""
            val t3 = "Цена"
            val b1 = binding.transfertCar
            editTextPicker(
                t1,
                t2,
                t3,
                b1,
                100,
                9999,
                "Значение не может быть меньше 100",
                "Значение не может быть больше 9999",
            )
        }
        binding.fastBid.setOnClickListener {
            val t1 = "Ставка менее чем за час"
            val t2 = ""
            val t3 = "Цена"
            val b1 = binding.fastBid
            editTextPicker(
                t1,
                t2,
                t3,
                b1,
                100,
                99999,
                "Значение не может быть меньше 100",
                "Значение не может быть больше 99999",
            )
        }

        binding.clear.setOnClickListener {
            editor.putString(USER_ACCEPTED_SAVED, "false").apply()
            params = CalcClass().defoarams()
            viewModel.update(params)
            findNavController().navigate(R.id.action_advancedSettingsFragment_self)
        }

        binding.save.setOnClickListener {
            params.freightVL = forEditebleText(binding.freight)
            params.FOB = forEditebleText(binding.fob)
            params.fastBid = forEditebleText(binding.fastBid)
            params.transfertCar = forEditebleText(binding.transfertCar)
            params.nego = forEditebleText(binding.nego)
            params.newCustomer = forEditebleText(binding.newCustomer)
            params.util = forEditebleText(binding.util)
            params.customsClearance = forEditebleText(binding.customsClearance)
            params.sbkts = forEditebleText(binding.sbkts)
            params.svh = forEditebleText(binding.svh)
            params.lab = forEditebleText(binding.lab)
            params.transfertTK = forEditebleText(binding.transfertTK)
            params.broker = forEditebleText(binding.broker)
            params.glonas = forEditebleText(binding.glonas)
            params.registr = forEditebleText(binding.registr)
            params.myFee = forEditebleText(binding.myFee)


            params.finalPrice = CalcClass().finalPrice(params)
            //  binding.fob.setText("${FOB2}")
            binding.japanMoney.setText("${params.japanMoney}")
            binding.rusMoney.setText("${params.rusMoney}")
            viewModel.update(params)
            findNavController().navigate(R.id.action_advancedSettingsFragment_to_navigation_dashboard)
        }
        binding.sv.setOnClickListener {
            val url = "https://auc.aleado.com/auctions/?p=project/searchform&searchtype=max&s&ld"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }


    private fun forEditebleText(editableText: TextInputEditText): Int {
        var g = 0

        if (editableText.editableText.toString() != "") {
            g = editableText.editableText.toString().toInt()
        }
        return g
    }

    private fun editTextPicker(
        t1: String,
        t2: String,
        t3: String,
        b1: EditText,
        min: Int,
        max: Int,
        minText: String,
        maxText: String,
    ) {
        var string = ""
        val d = AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.text_input_dialog, null)
        d.setTitle(t1)
        d.setMessage(
            t2
        )
        d.setView(dialogView)
        val mEditText = dialogView.findViewById<TextInputEditText>(R.id.editTextName)
        val mEditText2 = dialogView.findViewById<TextInputLayout>(R.id.textInputLayout)
        var enabled = false
        mEditText.maxEms = 6
        mEditText.addTextChangedListener(AppTextWatcher {
            string = mEditText.text.toString()
            if (string != "") {
                if (string.toInt() < min || string.toInt() != 0) {
                    mEditText2.error = minText
                    enabled = false
                }
                if (string.toInt() in min..max || string.toInt() == 0) {
                    mEditText2.isErrorEnabled = false
                    enabled = true
                }
                if (string.toInt() > max) {
                    mEditText2.error = maxText
                    enabled = false
                }
            }

        })
        d.setPositiveButton("Выбрать") { dialogInterface, i ->
            if (enabled) {
                b1.text = mEditText.editableText

                Toast.makeText(
                    requireContext(),
                    "$t3: ${mEditText.editableText}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        d.setNegativeButton("Отменить") { dialogInterface, i -> }
        val alertDialog = d.create()
        alertDialog.show()
    //return mEditText.editableText.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}