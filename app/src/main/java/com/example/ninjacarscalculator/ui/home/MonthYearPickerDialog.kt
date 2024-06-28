package com.example.ninjacarscalculator.ui.home

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.ninjacarscalculator.databinding.DialogMonthYearPickerBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class MonthYearPickerDialog(val date: Date = Date()) : DialogFragment() {
    var sdf = SimpleDateFormat("yyyy")
    var currentDate = sdf.format(Date()).toInt()


        private val MAX_YEAR = currentDate


    private lateinit var binding: DialogMonthYearPickerBinding

    private var listener: DatePickerDialog.OnDateSetListener? = null

    fun setListener(listener: DatePickerDialog.OnDateSetListener?) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogMonthYearPickerBinding.inflate(requireActivity().layoutInflater)
        val cal: Calendar = Calendar.getInstance().apply { time = date }

        binding.pickerMonth.run {
            minValue = 0
            maxValue = 11
            value = cal.get(Calendar.MONTH)
            displayedValues = arrayOf("Jan","Feb","Mar","Apr","May","June","July",
                "Aug","Sep","Oct","Nov","Dec")
        }

        binding.pickerYear.run {
            val year = cal.get(Calendar.YEAR)
            val valueYear = year-3
            val valueMinYear = year-15
            minValue = valueMinYear
            maxValue = year
            value = valueYear
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Выбери Дату производства")
            .setView(binding.root)
            .setPositiveButton("Выбрать") { _, _ -> listener?.onDateSet(null, binding.pickerYear.value, binding.pickerMonth.value, 1) }
            .setNegativeButton("Отменить") { _, _ -> dialog?.cancel() }
            .create()
    }
}