package com.example.todolist.ui.notice.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.notice.model.Notice
import com.example.todolist.databinding.FragmentNoticeListItemBinding
import com.example.todolist.ui.notice.adapter.diff.NoticeDiffCallback
import com.example.todolist.ui.notice.viewmodel.NoticeViewModel

class NoticeRecyclerAdapter(var notices: List<Notice>, private val viewModel: NoticeViewModel) :
    RecyclerView.Adapter<NoticeRecyclerAdapter.NoticeViewHolder>() {


    class NoticeViewHolder(private val binding: FragmentNoticeListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notice: Notice, viewModel: NoticeViewModel) {
            binding.notice = notice
            binding.executePendingBindings()
            binding.viewModel = viewModel
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentNoticeListItemBinding.inflate(layoutInflater, parent, false)
        return NoticeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bind(notices[position], viewModel)
    }

    override fun getItemCount() = notices.size

    fun updateNotice(newNotices: List<Notice>) {
        val diffCallback = NoticeDiffCallback(this.notices, newNotices)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.notices = newNotices
        diffResult.dispatchUpdatesTo(this)
    }

    fun removeItem(position: Int) {
        val mutableNotices = notices.toMutableList()
        mutableNotices.removeAt(position)
        notices = mutableNotices
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mutableNotices.size)
    }
}