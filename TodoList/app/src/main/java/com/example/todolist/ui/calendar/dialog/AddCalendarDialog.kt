package com.example.todolist.ui.calendar.dialog

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.core.util.Pair
import androidx.fragment.app.DialogFragment
import com.example.todolist.R
import com.example.todolist.databinding.FragmentAddCalendarDialogBinding
import com.example.todolist.ui.calendar.CalendarFragment
import com.example.todolist.ui.calendar.viewmodel.CalendarViewModel
import com.example.todolist.ui.main.viewmodel.MainViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddCalendarDialog () : DialogFragment() {

    private lateinit var binding : FragmentAddCalendarDialogBinding

    private lateinit var snackbar : Snackbar

    companion object {
        const val DEFAULT_STRING = ""
        const val DEFAULT_INT = -1
    }

    private var startDay : String = DEFAULT_STRING
    private var endDay : String = DEFAULT_STRING
    private var startTime : Time = Time(DEFAULT_INT,DEFAULT_INT)
    private var endTime : Time = Time(DEFAULT_INT,DEFAULT_INT)

    inner class Time(
        var hour : Int,
        var minute : Int
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCalendarDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDialog()
        setSnackbar()
        setSelectedTitle(arguments?.getString("title")?:"")

        binding.buttonInnerDate.setOnClickListener {
            hideKeyboard(view)
            showDateRangePicker()
        }

        binding.buttonInnerStartTime.setOnClickListener {
            hideKeyboard(view)
            showTimePicker{hour, minute ->
                unlockInnerEndTimeBtn()
                setStartTime(hour,minute)
                setEndTime(hour,minute)
            }
        }

        binding.buttonInnerEndTime.setOnClickListener {
            hideKeyboard(view)
            showTimePicker{hour, minute ->
                if (checkEndTimeProper(hour,minute)) setEndTime(hour,minute)
            }
        }

        lockInnerEndTimeBtn()
    }

    private fun setDialog() {
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        binding.buttonDialogPositive.setOnClickListener {
            if (checkInputDataProper()) {
                uploadNaverCalendar()
                dismiss()
            }
        }
        binding.buttonDialogNegative.setOnClickListener {
            dismiss()
        }
        dialog?.window?.decorView?.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val currentFocus = dialog?.currentFocus
                if (currentFocus is EditText) {
                    hideKeyboard(currentFocus)
                }
            }
            v.performClick()
            false
        }
    }

    private fun setSelectedTitle(title : String) {
        binding.editTextInnerTitle.setText(title)
    }

    private fun hideKeyboard(view : View?) {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
        view?.clearFocus()
    }

    private fun checkInputDataProper() : Boolean {
        if(binding.editTextInnerTitle.text.isBlank()) {
            showSnackbar(getString(R.string.add_calendar_dialog_not_proper_title_msg))
            return false
        }
        else if(startDay == DEFAULT_STRING || endDay == DEFAULT_STRING) {
            showSnackbar(getString(R.string.add_calendar_dialog_not_proper_date_msg))
            return false
        }
        else if (startTime.hour == DEFAULT_INT || startTime.minute == DEFAULT_INT) {
            showSnackbar(getString(R.string.add_calendar_dialog_not_proper_time_msg))
            return false
        }
        return true
    }

    private fun setSnackbar() {
        snackbar = Snackbar.make(binding.root,"",Snackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view
        val params = snackbarView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
    }
    private fun showSnackbar(msg : String) {
        snackbar.setText(msg).show()
    }

    private fun lockInnerEndTimeBtn() {
        binding.buttonInnerEndTime.isClickable = false
        binding.buttonInnerEndTime.background = context?.let{
            AppCompatResources.getDrawable(it, R.drawable.dialog_inner_item)
        }
        binding.buttonInnerEndTime.backgroundTintList = ColorStateList.valueOf(context?.getColor(R.color.bright_gray)?:0)
        binding.textViewInnerEndTime.text = getString(R.string.add_calendar_dialog_lock_msg)
    }
    private fun unlockInnerEndTimeBtn() {
        binding.buttonInnerEndTime.isClickable = true
        binding.buttonInnerEndTime.background = context?.let{
            AppCompatResources.getDrawable(it, R.drawable.dialog_inner_button)
        }
        binding.buttonInnerEndTime.backgroundTintList = null
    }

    private fun checkEndTimeProper(hour: Int, minute: Int) : Boolean {
        if(startTime.hour > hour || (startTime.hour == hour && startTime.minute > minute)) {
            return false
        }
        return true
    }

    private fun showDateRangePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("기간 선택")
            .setSelection(
                Pair(
                    MaterialDatePicker.todayInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds())
            )
        val dataRangePicker = builder.build()
        dataRangePicker.addOnPositiveButtonClickListener {
            val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            startDay = dateFormat.format(Date(it.first))
            endDay = dateFormat.format(Date(it.second))
            updateDateText(startDay, endDay)
        }
        dataRangePicker.show(parentFragmentManager,dataRangePicker.toString())
    }

    private fun showTimePicker(setTime : (Int, Int) -> Unit) {
        val isSystem24Hour = is24HourFormat(context)
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val builder =
            MaterialTimePicker.Builder()
                .setInputMode(INPUT_MODE_KEYBOARD)
                .setTimeFormat(if (true) clockFormat else TimeFormat.CLOCK_12H)
                .setTitleText("시간 선택")
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener {
            setTime.invoke(picker.hour, picker.minute)
        }
        picker.show(parentFragmentManager, picker.toString())
    }

    private fun setStartTime(hour : Int, minute : Int) {
        startTime.hour = hour
        startTime.minute = minute
        updateStartTimeText(hour, minute)
    }

    private fun setEndTime(hour : Int, minute : Int) {
        endTime.hour = hour
        endTime.minute = minute
        updateEndTimeText(hour, minute)
    }

    private fun updateDateText(start : String, end : String) {
        binding.textViewInnerDate.text = "${start} ~ ${end}"
    }

    private fun updateStartTimeText(hour : Int, minute : Int) {
        binding.textViewInnerStartTime.text = String.format(Locale.US,"%d:%02d",hour,minute)
    }

    private fun updateEndTimeText(hour : Int, minute : Int) {
        binding.textViewInnerEndTime.text = String.format(Locale.US,"%d:%02d",hour,minute)
    }

    private fun uploadNaverCalendar() {
        val startTime = String.format(Locale.US,"%02d%02d00",startTime.hour,startTime.minute)
        val endTime = String.format(Locale.US,"%02d%02d00",endTime.hour,endTime.minute)
        val icalendarData = Bundle().apply {
            putString("title", binding.editTextInnerTitle.text.toString())
            putString("detail", binding.editTextInnerDetail.text.toString())
            putString("startDay", startDay)
            putString("endDay", endDay)
            putString("startTime", startTime)
            putString("endTime", endTime)
        }
        parentFragmentManager.setFragmentResult("icalendarData", icalendarData)
    }
}