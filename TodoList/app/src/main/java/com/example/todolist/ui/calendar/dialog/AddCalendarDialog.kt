package com.example.todolist.ui.calendar.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.todolist.databinding.FragmentAddCalendarDialogBinding

class AddCalendarDialog () : DialogFragment() {

    private lateinit var binding: FragmentAddCalendarDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCalendarDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialog()
    }

    fun setDialog() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        binding.dialogPositiveBtn.setOnClickListener {
            setFragmentResult("result",
                Bundle().apply{
                    putString("result", "")
                })
            dismiss()
        }
        binding.dialogNegativeBtn.setOnClickListener {
            dismiss()
        }
    }
}