package com.example.todolist.ui.calendar.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.data.TodoEntity
import com.example.todolist.databinding.ActivityCalendarTodoListItemBinding
import com.example.todolist.ui.calendar.adapter.diffutil.TodoDiffUtil
import java.text.SimpleDateFormat
import java.util.Locale

class CalendarTodoListRecyclerAdapter (
    private var items : List<TodoEntity>,
    private var listener : OnItemClickListener
) : RecyclerView.Adapter<CalendarTodoListRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: ActivityCalendarTodoListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ActivityCalendarTodoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onItemClick(title : String, date : String) {}
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            binding.textViewTodoTitle.text = items[position].todo
            binding.textViewTodoDate.text = dateFormat(items[position].date)
            binding.todoItemLy.setOnClickListener{
                listener.onItemClick(items[position].todo, items[position].date.toString())
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun dateFormat(dateInt : Int) : String{
        val date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).parse(dateInt.toString())
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
    }

    fun update(newItems: List<TodoEntity>) {
        val diffUtil = TodoDiffUtil(items, newItems)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffUtil)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }
}