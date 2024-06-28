package com.example.ninjacarscalculator.ui.settings

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.example.ninjacarscalculator.R
import com.example.ninjacarscalculator.calculations.CalcClass
import com.example.ninjacarscalculator.database.AllParametrs
import com.example.ninjacarscalculator.databinding.FragmentSettingsBinding
import com.example.ninjacarscalculator.ui.login.AppTextWatcher
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.floor

class SettingsFragment : Fragment() {
    private val PREFERENCES_COUNTER_NAME = "settings"
    private val USER_ACCEPTED_NAME = "name"
    private val USER_ACCEPTED_NUMB = "numb"
    private val USER_ACCEPTED_SAVED = "saved"
    private val USER_ACCEPTED_MON = "mon"
    private lateinit var mAuth: FirebaseAuth
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private var clicked = false
    private lateinit var prefs: SharedPreferences
    private lateinit var viewModel: SettingsViewModel
 var params: AllParametrs =  AllParametrs( 1,2021,660,730000,60000,60000, 0.0, 0, 0, 0,0.0,20000,0,0, 0, 0,60000, 0,0,0, 0,5200,3100, 20000,30000,5000,0,10000, 0,0,25000,100.0, 90.0,0.6)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       viewModel =
           ViewModelProvider(this, ViewModelFactoryS(requireActivity()))[SettingsViewModel::class.java]
        mAuth = FirebaseAuth.getInstance()
        prefs =
            requireContext().getSharedPreferences(PREFERENCES_COUNTER_NAME, Context.MODE_PRIVATE)

        val saved = prefs.getString(USER_ACCEPTED_SAVED, "false")
        val text = "Это поле расчитывается автоматически"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(requireContext(), text, duration)
        val editor = prefs.edit()
        val animAlpha: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.alpha)
        binding.settingsj.setOnClickListener {

            if (mAuth.currentUser == null) {
                val bundle = Bundle()
                bundle.putString("fragment", "settings")
                findNavController().navigate(R.id.action_settingsFragment_to_loginFragment,bundle)
            }else {
                findNavController().navigate(R.id.action_settingsFragment_to_advancedSettingsFragment)
            }
            }
        viewModel.echangeRates.onEach {

            if (it != null) {

                params.euro = floor("${it.Valute.EUR.Value}".toDouble()*100)/100
                params.usd = floor("${it.Valute.USD.Value}".toDouble()*100)/100
                params.yen = floor("${it.Valute.JPY.Value.div(100)}".toDouble()*100)/100
            }

        }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.params.onEach {
            if (it.isNotEmpty()) {
                params = it.firstOrNull()!!
                binding.fob.setText(it.firstOrNull()!!.FOB.toString())
                Log.d("paraps1", params.toString())

                with(binding) {
                    yen.text = "¥ - ${params.yen}"
                    usd.text = "$ - ${params.usd}"
                    euro.text = "Э - ${params.euro}"



                       addServices.setText(params.addServices.toString())
                       deliveryIty.setText(params.deliveryСity.toString())
                       buttonComission.setText(params.buttonComission.toString())
                       banksOmmission.setText(params.banksСommission.toString())




                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)


        binding.fob.setOnClickListener {

            val t1 = "FOB  (Расходы по Японии)"
            val t2 = ""
            val t3 = "Цена"
            val b1 = binding.fob
            //fob
            editTextPickerI(
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
        binding.update.setOnClickListener {
            if (!clicked) {
                clicked = true
                viewModel.loadExchange()
                viewModel.echangeRates.onEach {exchangeRates->
                    if(exchangeRates?.Valute?.EUR?.Value!=null) {
                        with(binding) {
                            yen.text = "¥ - ${floor(exchangeRates?.Valute?.JPY?.Value?.div(100)!!*100)/100}"
                            usd.text = "$ - ${floor(exchangeRates?.Valute?.USD?.Value!!*100)/100}"
                            euro.text = "Э - ${floor(exchangeRates?.Valute?.EUR?.Value!!*100)/100}"
                        }
                    }

                }.launchIn(viewLifecycleOwner.lifecycleScope)
            } else {

                editTextPicker("Введите курсы валют", "",params.yen,params.usd,params.euro, )

            }


        }
        binding.addServices.setOnClickListener {
            val t1 = "Дополнительные услуги"
            val t2 = ""
            val t3 = ""
            val b1 = binding.addServices
            editTextPickerI(
                t1,
                t2,
                t3,
                b1,
                1000,
                999999,
                "значение не может быть меньше 1000",
                "значение не может быть больше 999999"
            )
        }

        binding.deliveryIty.setOnClickListener {
            val t1 = "Доставка в другой город"
            val t2 = ""
            val t3 = ""
            val b1 = binding.deliveryIty
            editTextPickerI(
                t1,
                t2,
                t3,
                b1,
                1000,
                999999,
                "значение не может быть меньше 1000",
                "значение не может быть больше 999999"
            )
        }
        binding.buttonComission.setOnClickListener {
            val t1 = "Комиссия за кнопку "
            val t2 = ""
            val t3 = ""
            val b1 = binding.buttonComission
            editTextPickerI(
                t1,
                t2,
                t3,
                b1,
                100,
                99999,
                "значение не может быть меньше 100",
                "значение не может быть больше 99999"
            )
        }
        binding.banksOmmission.setOnClickListener {
            val t1 = "Комиссия банка"
            val t2 = ""
            val t3 = "%"
            val b1 = binding.banksOmmission
            editTextPickerC(
                t1,
                t2,
                t3,
                b1,
                0.001,
                20.0,
                "комиссия не может быть меньше 0.001%",
                "Вам следует выбрать другой банк"
            )
        }

        binding.clear.setOnClickListener {
            editor.putString(USER_ACCEPTED_SAVED, "false").apply()
            params = CalcClass().defoarams()
            viewModel.update(params)
            findNavController().navigate(R.id.action_settingsFragment_self)
        }
binding.save.setOnClickListener {
    if (binding.addServices.editableText.toString()!="") {
        params.addServices = binding.addServices.editableText.toString().toInt()
    }
    if (binding.deliveryIty.editableText.toString()!="") {
        params.deliveryСity = binding.deliveryIty.editableText.toString().toInt()
    }
        if (binding.buttonComission.editableText.toString()!="") {
            params.buttonComission = binding.buttonComission.editableText.toString().toInt()
        }
            if (binding.banksOmmission.editableText.toString()!="") {
                params.banksСommission = binding.banksOmmission.editableText.toString().toDouble()
            }

    if (binding.fob.editableText.toString()!="") {
        params.FOB = binding.fob.editableText.toString().toInt()
    }
    viewModel.update(params)
    findNavController().navigate(R.id.action_settingsFragment_to_navigation_dashboard)
}
        binding.sv.setOnClickListener {
            val url = "https://auc.aleado.com/auctions/?p=project/searchform&searchtype=max&s&ld"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        binding.priceauto.setOnClickListener {
            val url = "https://auc.aleado.com/auctions/?p=project/searchform&searchtype=max&s&ld"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }


    }

    private fun editTextPickerI(
        t1: String, t2: String,t3: String, b1: EditText, min: Int,
        max: Int,minText: String,maxText:String
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
            if(string !="") {
                if (string.toInt() < min || string.toInt() != 0) {
                    mEditText2.error = minText
                    enabled = false
                }
                if (string.toInt() in min..max || string.toInt() == 0) {
                    mEditText2.isErrorEnabled = false
                    enabled = true
                }
                if (string.toInt() > max) {
                    mEditText2.error = minText
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
    }
    private fun editTextPickerC(
        t1: String, t2: String,t3: String, b1: EditText, min: Double,
        max: Double,minText: String,maxText:String
    ) {
        var string = ""
        val d = AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.text_input_dialog_double, null)
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
            if(string !="") {
                if (string.toDouble() < min || string.toDouble() != 0.0) {
                    mEditText2.error = minText
                    enabled = false
                }
                if (string.toDouble() in min..max || string.toDouble() == 0.0) {
                    mEditText2.isErrorEnabled = false
                    enabled = true
                }
                if (string.toDouble() > max) {
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
    }

    private fun editTextPicker(t1: String, t2: String, curs1: Double, curs2: Double, curs3: Double,  ) {
        val d = AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_exchange_dialog, null)
        d.setTitle(t1)
        d.setMessage(
            t2
        )
        d.setView(dialogView)
        val textInput = dialogView.findViewById<EditText>(R.id.text_input1)
        val textInput2 = dialogView.findViewById<EditText>(R.id.text_input2)
        val textInput3 = dialogView.findViewById<EditText>(R.id.text_input3)

            textInput.setText("$curs1")
            textInput2.setText("$curs2")
            textInput3.setText("$curs3")


        d.setPositiveButton("Сохранить") { dialogInterface, i ->
            with(binding) {
                yen.text = "¥ - ${textInput.editableText}"
                usd.text = "$ - ${textInput2.editableText}"
                euro.text = "Э - ${textInput3.editableText}"

                if (textInput.editableText.toString()!="") {
                    params.yen = textInput.editableText.toString().toDouble()
                }
                if (textInput2.editableText.toString()!="") {
                    params.usd = textInput2.editableText.toString().toDouble()
                }
                    if (textInput3.editableText.toString()!="") {
                        params.euro = textInput3.editableText.toString().toDouble()
                    }


            }

        }
        d.setNegativeButton("Отменить") { dialogInterface, i -> }
        val alertDialog = d.create()
        alertDialog.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}