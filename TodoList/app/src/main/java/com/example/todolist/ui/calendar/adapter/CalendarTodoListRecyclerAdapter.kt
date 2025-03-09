package com.example.todolist.ui.calendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.todo.model.Todo
import com.example.todolist.databinding.ActivityCalendarTodoListItemBinding
import com.example.todolist.ui.calendar.adapter.diffutil.TodoDiffUtil

class CalendarTodoListRecyclerAdapter (
    private var items : List<Todo>,
    private var listener : OnItemClickListener
) : RecyclerView.Adapter<CalendarTodoListRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: ActivityCalendarTodoListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ActivityCalendarTodoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onItemClick() {}
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            binding.todoTitleTv.text = items[position].title
            binding.todoDateTv.text = items[position].date
            binding.todoItemLy.setOnClickListener{
                listener.onItemClick()
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun update(newItems: List<Todo>) {
        val diffUtil = TodoDiffUtil(items, newItems)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffUtil)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }
}