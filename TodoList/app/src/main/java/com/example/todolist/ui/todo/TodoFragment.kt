package com.example.todolist.ui.todo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.data.todo.TodoDatabase
import com.example.data.todo.TodoRepositoryImpl
import com.example.domain.todo.TodoEntity
import com.example.domain.todo.repository.TodoRepository
import com.example.todolist.R
import com.example.todolist.ui.todo.viewmodel.*
import com.example.todolist.databinding.FragmentTodoBinding
import com.example.todolist.ui.main.MainActivity
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TodoFragment : Fragment() {
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TodoViewModel
    private lateinit var adapter: TodoAdapter

    private val dateFormatInt = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    private val dateFormatDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val calendar = Calendar.getInstance()

    @Inject
    lateinit var repository: TodoRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel 설정
        val database = TodoDatabase.getDatabase(requireContext())
        val repository = TodoRepositoryImpl(database.todoDao())
        val factory = TodoViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TodoViewModel::class.java]

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

        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return 0.45f
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
                val itemView = viewHolder.itemView
                val threshold = 0.45f * recyclerView.width

                val itemHeight = itemView.bottom - itemView.top

                val swipeWidth = Math.min(Math.abs(dX), itemView.width.toFloat())

                val backgroundColor = if (kotlin.math.abs(dX) >= threshold) {
                    Color.parseColor("#E82561") // 빨간색 배경 (삭제)
                } else {
                    Color.parseColor("#D3D3D3") // 회색 배경 (기본 상태)
                }

                // 배경 색상 그리기
                val swipeRect = RectF(
                    itemView.right + dX,
                    itemView.top.toFloat(),
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat()
                )
                val backgroundPaint = Paint().apply {
                    color = backgroundColor
                }
                c.drawRect(swipeRect, backgroundPaint)

                val icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)
                icon?.let {
                    val intrinsicWidth = it.intrinsicWidth
                    val intrinsicHeight = it.intrinsicHeight
                    val iconMargin = 20.dpToPx(requireContext())
                    val iconFixedPosition = 60.dpToPx(requireContext())
                    val left = if (Math.abs(dX) <= iconFixedPosition) itemView.right - swipeWidth + iconMargin else itemView.right - iconFixedPosition + iconMargin
                    val right = left + intrinsicWidth
                    val top = itemView.top + (itemHeight - intrinsicHeight) / 2
                    val bottom = top + intrinsicHeight
                    it.setBounds(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
                    it.draw(c)
                }

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.alpha = 1.0f
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun Int.dpToPx(context: Context): Float {
        return this * context.resources.displayMetrics.density
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