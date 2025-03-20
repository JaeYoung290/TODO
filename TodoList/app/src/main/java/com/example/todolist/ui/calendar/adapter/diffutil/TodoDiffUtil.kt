package com.example.todolist.ui.calendar.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.data.TodoEntity

class TodoDiffUtil (
    private val oldList: List<TodoEntity>,
    private val newList: List<TodoEntity>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.todo == newItem.todo &&
                oldItem.date == newItem.date &&
                oldItem.checked == newItem.checked
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}