package com.example.todolist.ui.todo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.todo.TodoEntity
import com.example.domain.todo.repository.TodoRepository
import com.example.todolist.ui.todo.viewmodel.*
import com.example.todolist.databinding.FragmentTodoBinding
import com.example.todolist.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class TodoFragment : Fragment() {
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TodoViewModel by viewModels()
    private lateinit var adapter: TodoAdapter

    private val dateFormatInt = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    private val dateFormatDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val calendar = Calendar.getInstance()

    // test
    @Inject
    lateinit var repository: TodoRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        // test
        Log.d("HiltTest", "TodoRepository 주입됨: $repository")
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        adapter = TodoAdapter(mutableListOf(), activity as MainActivity)
        binding.recyclerViewTodoList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTodoList.adapter = adapter

        attachSwipeToDelete(binding.recyclerViewTodoList)
        updateTodoList()

        binding.textViewLeft.setOnClickListener {
            saveTodos()
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            updateTodoList()
        }

        binding.textViewRight.setOnClickListener {
            saveTodos()
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            updateTodoList()
        }

        binding.textViewDate.setOnClickListener {
            saveTodos()
            binding.calendarViewCalendar.date = calendar.timeInMillis
            binding.calendarViewCalendar.visibility = View.VISIBLE
            binding.linearLayoutTouchBlocker.visibility = View.VISIBLE
        }

        binding.calendarViewCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            updateTodoList()
            binding.calendarViewCalendar.visibility = View.GONE
            binding.linearLayoutTouchBlocker.visibility = View.GONE
        }

        binding.linearLayoutTouchBlocker.setOnClickListener {
            binding.calendarViewCalendar.visibility = View.GONE
            binding.linearLayoutTouchBlocker.visibility = View.GONE
        }

        binding.recyclerViewTodoList.setOnTouchListener { _, _ ->
            val view = requireActivity().currentFocus
            if (view is EditText) {
                view.clearFocus()
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
            false
        }

        binding.calendarViewCalendar.visibility = View.GONE
        binding.linearLayoutTouchBlocker.visibility = View.GONE
    }

    private fun attachSwipeToDelete(recyclerView: RecyclerView) {

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return 0.65f
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                adapter.removeItem(position)
            }

            @SuppressLint("ObsoleteSdkInt")
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val threshold = 0.65f * recyclerView.width

                val limitedDX = if (kotlin.math.abs(dX) > threshold) {
                    threshold * if (dX > 0) 1 else -1  // 방향 유지
                } else {
                    dX
                }

                val alpha = 1.0f - (kotlin.math.abs(limitedDX) / recyclerView.width) * 1.53
                viewHolder.itemView.alpha = alpha.toFloat()
                super.onChildDraw(c, recyclerView, viewHolder, limitedDX, dY, actionState, isCurrentlyActive)
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.alpha = 1.0f
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun saveTodos() {
        val currentView = requireActivity().currentFocus
        if (currentView != null) {
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentView.windowToken, 0)
            currentView.clearFocus()
        }

        val selectedDate = dateFormatInt.format(calendar.time).toInt()
        val todos = ArrayList(adapter.getItemList())
        todos.removeAt(todos.size - 1)

        viewModel.deleteTodosByDate(selectedDate)
        viewModel.insertTodos(todos)
    }

    private fun updateTodoList() {
        val selectedDate = dateFormatInt.format(calendar.time).toInt()
        binding.textViewDate.text = dateFormatDate.format(calendar.time)

        viewModel.getTodosByDate(selectedDate) { todos ->
            activity?.runOnUiThread {
                val lists = todos.toMutableList().apply {
                    add(TodoEntity(date = selectedDate, checked = false, todo = ""))
                }
                adapter.updateData(lists)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        saveTodos()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}