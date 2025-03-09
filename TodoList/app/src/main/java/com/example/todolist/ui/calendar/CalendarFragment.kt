package com.example.todolist.ui.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.domain.todo.model.Todo
import com.example.todolist.databinding.FragmentCalendarBinding
import com.example.todolist.ui.calendar.adapter.CalendarTodoListRecyclerAdapter
import com.example.todolist.ui.calendar.dialog.AddCalendarDialog
import com.example.todolist.ui.calendar.viewmodel.CalendarViewModel


class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding

    private lateinit var viewModel: CalendarViewModel

    private lateinit var adapter: CalendarTodoListRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        viewModel = CalendarViewModel()
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 임시 test 용 item
        viewModel.todoList.add(Todo("Test", false, "Test"))
        viewModel.todoList.add(Todo("Test", false, "Test"))
        viewModel.todoList.add(Todo("Test", false, "Test"))

        adapter = CalendarTodoListRecyclerAdapter(viewModel.todoList, object : CalendarTodoListRecyclerAdapter.OnItemClickListener{
            override fun onItemClick() {
                super.onItemClick()
                onItemClickDialog()
            }
        })
        binding.todoList.adapter = adapter
        binding.todoList.addItemDecoration(DividerItemDecoration(context, 1))
        adapter.update(viewModel.todoList)

        lockUntilNaverLogin()
    }

    private fun onItemClickDialog() {
        val dialog = AddCalendarDialog()
        parentFragmentManager.setFragmentResultListener("result", this) {Key, bundle ->
            val result = bundle.getString("result")
            result?.run{
            }
        }
        dialog.show(parentFragmentManager,"")
    }

    private fun lockUntilNaverLogin(){
        binding.todoList.isVisible = false
    }

    private fun unlockAfterNaverLogin(){
        binding.naverLoginTv.isVisible = false
        binding.naverLoginBtn.isVisible = false
        binding.todoList.isVisible = true
    }
}