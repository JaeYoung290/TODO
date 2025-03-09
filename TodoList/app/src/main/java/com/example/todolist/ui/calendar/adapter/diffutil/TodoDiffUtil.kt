package com.example.todolist.ui.calendar.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.todo.model.Todo

class TodoDiffUtil (
    private val oldList: List<Todo>,
    private val newList: List<Todo>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.title == newItem.title &&
                oldItem.date == newItem.date &&
                oldItem.isChecked == newItem.isChecked
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}