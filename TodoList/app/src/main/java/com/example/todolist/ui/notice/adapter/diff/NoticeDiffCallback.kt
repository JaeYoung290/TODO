package com.example.todolist.ui.notice.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.notice.model.Notice

class NoticeDiffCallback(private val oldList: List<Notice>, private val newList: List<Notice>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}