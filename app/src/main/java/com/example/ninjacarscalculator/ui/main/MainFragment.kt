package com.example.ninjacarscalculator.ui.main


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.example.ninjacarscalculator.R
import com.example.ninjacarscalculator.calculations.CalcClass
import com.example.ninjacarscalculator.models.Task
import com.example.ninjacarscalculator.databinding.FragmentMainBinding
import com.example.ninjacarscalculator.models.UserData
import com.example.ninjacarscalculator.ui.home.MonthYearPickerDialog
import com.example.ninjacarscalculator.ui.login.AppTextWatcher
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.math.floor
import kotlin.reflect.KClass


class MainFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val PREFERENCES_COUNTER_NAME = "settings"
    private val USER_ACCEPTED_NAME = "name"
    private val USER_ACCEPTED_NUMB = "numb"
    private val USER_ACCEPTED_SAVED = "saved"
    private val USER_ACCEPTED_MON = "mon"
    private lateinit var prefs: SharedPreferences
    var saveAutoname: MutableList<Task> = mutableListOf()
    var customF2 = ""
    var customF = 0.0
    var userData: UserData? = null
    var value = 0
    var FOB2 = 0
    var name = "name"
    private lateinit var taskMap: Map<String, Any>
    var save = false

    var numb = "numb"
    val user = FirebaseAuth.getInstance().currentUser
    var dbReference = FirebaseDatabase.getInstance().getReference("users")
    var params = CalcClass().defoarams()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(
                this,
                ViewModelFactoryM(requireActivity())
            )[MainViewModel::class.java]
        prefs =
            requireContext().getSharedPreferences(PREFERENCES_COUNTER_NAME, Context.MODE_PRIVATE)
        mAuth = FirebaseAuth.getInstance()
        val animAlpha: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.alpha)
        val editor = prefs.edit()
        val firstName = prefs.getString(USER_ACCEPTED_NAME, "User")
        val firstNumb = prefs.getString(USER_ACCEPTED_NUMB, "Numb")
        val date1 = prefs.getString("date", "User")
        val engine1 = prefs.getString("engine", "Numb")
        val price1 = prefs.getString("price", "User")

        val saved = prefs.getString(USER_ACCEPTED_SAVED, "false")
        if (firstName == "User" || firstName == null || firstName == "null") {
            name = arguments?.getString("name").toString()
            numb = arguments?.getString("mPhoneNumber").toString()
            editor.putString(USER_ACCEPTED_NAME, name).apply()
            editor.putString(USER_ACCEPTED_NUMB, numb).apply()
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date()).toString()
            saveAutoname += Task(currentDate, "default_car", params)
            user?.run {
                val userData = UserData(
                    numb = numb,
                    name = name,
                    saveAutoname = saveAutoname
                )

                dbReference.child(user.uid).setValue(userData)
            }
        } else {
            name = firstName
            numb = firstNumb!!
            addUserChangeListener()
        }

        val text = "Это поле расчитывается автоматически"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(requireContext(), text, duration)



        binding.customsFee.setOnClickListener {
            toast.show()

            binding.customsFee.startAnimation(animAlpha)

        }
        binding.brokerageServicesRegistration.setOnClickListener {
            toast.show()
            binding.brokerageServicesRegistration.startAnimation(animAlpha)
        }
        binding.customsOefficient.setOnClickListener {
            toast.show()
            binding.customsOefficient.startAnimation(animAlpha)
        }
//        binding.customsFee.setOnClickListener {
//            toast.show()
//            binding.customsFee.startAnimation(animAlpha)}
        binding.brokerageServicesRegistration.setOnClickListener {
            toast.show()
            binding.brokerageServicesRegistration.startAnimation(animAlpha)
        }
        binding.customsOefficient.setOnClickListener {
            toast.show()
            binding.customsOefficient.startAnimation(animAlpha)
        }
        binding.finalPrice.setOnClickListener {
            toast.show()
            binding.finalPrice.startAnimation(animAlpha)
        }



        binding.history.setOnClickListener {
            if (isOnline(requireContext())) {
                findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_notifications)
            } else {
                val text = "Проверте подключение к интернету"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(requireContext(), text, duration)
                toast.show()
            }
        }
        binding.settings.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_dashboard_to_settingsFragment)
        }
        val date = Calendar.getInstance().time




        viewModel.echangeRates.onEach {

            if (it != null) {
                params.euro = floor("${it.Valute.EUR.Value}".toDouble() * 100) / 100
                params.usd = floor("${it.Valute.USD.Value}".toDouble() * 100) / 100
                params.yen = floor("${it.Valute.JPY.Value.div(100)}".toDouble() * 100) / 100
            }

        }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.params.onEach {
            if (it.isNotEmpty()) {
                params = it.firstOrNull()!!
                params.finalPrice = CalcClass().finalPrice(params)
                if (date1 == "true") {
                    val mon = prefs.getString(USER_ACCEPTED_MON, "1")
                    binding.datePicker.setText("${mon}/${params.dateManufact}")
                }
                if (engine1 == "true") {
                    binding.engineCapacity.setText(params.engineCapacity.toString())
                }
                if (price1 == "true") {
                    binding.auctionPrice.setText(params.carPrice.toString())
                }
                if (saved == "true") {
                    val mon = prefs.getString(USER_ACCEPTED_MON, "1")
//                    params.finalPrice = CalcClass().finalPrice(params)
                    binding.datePicker.setText("${mon}/${params.dateManufact}")
                    binding.engineCapacity.setText(params.engineCapacity.toString())
                    binding.auctionPrice.setText(params.carPrice.toString())
                    //   Log.d("fob", params.FOB.toString())
                    // binding.fob.setText(params.FOB.toString() )
                    binding.customsFee.setText(params.myFee.toString())
                    binding.brokerageServicesRegistration.setText(params.brokerageServicesRegistration.toString())
                    binding.customsOefficient.setText(params.customsСoefficient.toString())
                    binding.fraht.setText(params.freightVL.toString())
                    binding.customsFee.setText(params.customsFee.toString())
                    binding.brokerageServicesRegistration.setText(params.brokerageServicesRegistration.toString())
                    binding.customsOefficient.setText(CalcClass().customsFee(params).text)
                    binding.finalPrice.setText("${params.finalPrice}")
                }
                Log.d("fob", params.FOB.toString())
                binding.fob.setText(params.FOB.toString())
                binding.fraht.setText(params.freightVL.toString())


            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)



        binding.datePicker.setOnClickListener {

            MonthYearPickerDialog(date).apply {
                setListener { view, year, month, dayOfMonth ->
                    binding.datePicker.setText("${month + 1}/$year")
                    editor.putString(USER_ACCEPTED_MON, "${month + 1}").apply()
                    params.dateManufact = year
                    Toast.makeText(
                        requireContext(),
                        "Выбранная дата: ${month + 1}/$year",
                        Toast.LENGTH_LONG
                    ).show()
                }
                show(this@MainFragment.parentFragmentManager, "MonthYearPickerDialog")
            }
            editor.putString("date", "true").apply()

        }





        binding.engineCapacity.setOnClickListener {
            params.engineCapacity = numberPickerCustom()
            editor.putString("engine", "true").apply()
        }
        binding.auctionPrice.setOnClickListener {
            val t1 = "Цена покупки на аукционе"
            val t2 = ""
            val t3 = "Цена"
            val b1 = binding.auctionPrice
            editTextPicker(t1, t2, t3, b1, 100000, 9999999, "введите значение более 100 000")

            editor.putString("price", "true").apply()
        }
        binding.fraht.setOnClickListener {
            val t1 = "Фрахт до Владивостока"
            val t2 = ""
            val t3 = "Цена"
            val b1 = binding.fraht
            editTextPicker(
                t1,
                t2,
                t3,
                b1,
                5000,
                999999,
                "в текущее время цены на доставку около 60 000¥"
            )
            editor.putString(USER_ACCEPTED_SAVED, "true").apply()
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
                "в текущее время FOB выходит около 60 000¥"
            )
            editor.putString(USER_ACCEPTED_SAVED, "true").apply()
        }

        binding.bottom.setOnClickListener {


            editor.putString(USER_ACCEPTED_SAVED, "true").apply()
            if (binding.datePicker.editableText.toString() == "") {
                binding.datePicker.error = ""
                save = false
            } else {
                binding.datePicker.error = null

            }
            if (binding.engineCapacity.editableText.toString() == "") {
                binding.engineCapacity.error = ""
                save = false
            } else {
                binding.engineCapacity.error = null

            }


            if (binding.fraht.editableText.toString() != "") {
                params.freightVL = binding.fraht.editableText.toString().toInt()
                binding.fraht.error = null
                save = true
            } else {
                binding.fraht.error = ""
                save = false
            }
            if (binding.fob.editableText.toString() != "") {
                params.FOB = binding.fob.editableText.toString().toInt()
                save = true
                binding.fob.error = null
            } else {
                binding.fob.error = ""
                save = false
            }
            if (binding.auctionPrice.editableText.toString() != "") {
                params.carPrice = binding.auctionPrice.editableText.toString().toInt()
                save = true
                binding.auctionPrice.error = null
            } else {
                binding.auctionPrice.error = ""
                save = false
            }

            if (save) {
                params.finalPrice = CalcClass().finalPrice(params)
                binding.finalPrice.setText("${params.finalPrice}")
                binding.customsFee.setText(params.customsFee.toString())
                binding.brokerageServicesRegistration.setText(params.brokerageServicesRegistration.toString())
                binding.customsOefficient.setText(CalcClass().customsFee(params).text)
                viewModel.updateParam(params)
            }
        }
        binding.clear.setOnClickListener {
            editor.putString(USER_ACCEPTED_SAVED, "false").apply()
            params = CalcClass().defoarams()
            viewModel.updateParam(params)
            editor.putString("price", "false").apply()
            editor.putString("engine", "false").apply()
            editor.putString("date", "false").apply()
            findNavController().navigate(R.id.action_navigation_dashboard_self)
        }

        binding.bottomButton.setOnClickListener {
            val url = "https://auc.aleado.com/auctions/?p=project/searchform&searchtype=max&s&ld"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        binding.save.setOnClickListener {
            if (isOnline(requireContext())) {
                if (save || saved == "true") {
                    if (mAuth.currentUser == null) {
                        editor.putString(USER_ACCEPTED_SAVED, "true").apply()
                        val myDialogFragment = MyDialogFragment(
                            "Сохранять авто в журнал могут зарегестрированные пользователи",
                            "",
                            ""
                        )
                        val manager = requireActivity().supportFragmentManager
                        val transaction: FragmentTransaction = manager.beginTransaction()
                        myDialogFragment.show(transaction, "dialog")
                    } else {
                        editor.putString(USER_ACCEPTED_SAVED, "false").apply()
                        editAutoNamePicker()
                    }
                }
            } else {
                val text = "Проверте подключение к интернету"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(requireContext(), text, duration)
                toast.show()
            }

        }
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    private fun addUserChangeListener() {
        try {
            user?.run {
                val userIdReference = Firebase.database.reference
                    .child("users").child(uid)
                userIdReference.get().addOnSuccessListener {
                    taskMap = it.value as Map<String, Any>
                    //   var hj = it.getValue(ListTask::class.java)
                    userData = mapToObject(taskMap, UserData::class)
                    saveAutoname = userData!!.saveAutoname
                }
            }
        } catch (_: NumberFormatException) {
            IllegalArgumentException("")
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


    private fun editAutoNamePicker() {
        var string = ""
        val d = AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.auto_save, null)
        d.setTitle("Введите название авто")
        d.setView(dialogView)
        val mEditText = dialogView.findViewById<TextInputEditText>(R.id.editTextNameAuto)
        val mEditText2 = dialogView.findViewById<TextInputLayout>(R.id.textInputLayoutAuto)
        var enabled = false
        mEditText.addTextChangedListener(AppTextWatcher {
            string = mEditText.text.toString()
            if (string != "") {
                if (string.length < 3) {
                    mEditText2.error = "Слишком короткое название"
                    enabled = false
                } else {
                    mEditText2.isErrorEnabled = false
                    enabled = true
                }
            }

        })
        d.setPositiveButton("Выбрать") { dialogInterface, i ->
            if (enabled) {
                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                val currentDate = sdf.format(Date()).toString()

                user?.run {
                    val userIdReference = Firebase.database.reference
                        .child("users").child(user.uid)
                    val taskData = Task(
                        date = currentDate,
                        nameAuto = mEditText.editableText.toString(),
                        params = params


                    )
                    saveAutoname += taskData
                    userData = UserData(
                        numb = numb,
                        name = name,
                        saveAutoname = saveAutoname
                    )
                    Log.d("this", "${userData!!.numb} - ${userData!!.saveAutoname}")

                    userIdReference.setValue(userData)
                }
                findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_notifications)
                Toast.makeText(
                    requireContext(),
                    " ${mEditText.editableText}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        d.setNegativeButton("Отменить") { dialogInterface, i -> }
        val alertDialog = d.create()
        alertDialog.show()

    }


    private fun editTextPicker(
        t1: String,
        t2: String,
        t3: String,
        b1: EditText,
        min: Int,
        max: Int,
        minText: String,
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
                if (string.toInt() < min) {
                    mEditText2.error = minText
                    enabled = false
                }
                if (string.toInt() in min..max) {
                    mEditText2.isErrorEnabled = false
                    enabled = true
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

    private fun numberPickerCustom(): Int {
        val d = AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.number_picker_dialog, null)
        d.setTitle("Выберите обьем двигателя")
        d.setMessage(
            "Автомобилей с объемом двигателя меньше 658 сс нет на аукционе. \n" +
                    "Автомобили с объемом двигателя больше 1800 сс сейчас запрещены для экспорта в Россию"
        )
        d.setView(dialogView)
        val numberPicker = dialogView.findViewById<NumberPicker>(R.id.dialog_number_picker)
        val cc = arrayOf(
            "660",
            "700",
            "800",
            "900",
            "1000",
            "1100",
            "1200",
            "1300",
            "1400",
            "1500",
            "1600",
            "1700",
            "1800"
        )


        numberPicker.minValue = 0
        numberPicker.maxValue = cc.size - 1
        numberPicker.wrapSelectorWheel = false
        numberPicker.displayedValues = cc
        numberPicker.setOnValueChangedListener { numberPicker, i, i1 ->
            Log.d(
                "engineCapacity",
                cc[i1]
            )
        }
        d.setPositiveButton("Выбрать") { dialogInterface, i ->
            params.engineCapacity = cc[numberPicker.value].toInt()
            binding.engineCapacity.setText(cc[numberPicker.value].toString())
            Toast.makeText(
                requireContext(),
                "Выбранный объем: ${cc[numberPicker.value]}",
                Toast.LENGTH_LONG
            ).show()
        }
        d.setNegativeButton("Отменить") { dialogInterface, i -> }
        val alertDialog = d.create()
        alertDialog.show()
        return cc[numberPicker.value].toInt()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

