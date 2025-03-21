package com.example.todolist.ui.notice.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.notice.model.Notice
import com.example.todolist.databinding.FragmentNoticeListItemBinding

class NoticeRecyclerAdapter(private var dataSet: List<Notice>) :
    RecyclerView.Adapter<NoticeRecyclerAdapter.ViewHolder>() {
    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, url: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    fun updateData(newDataSet: List<Notice>) {
        this.dataSet = newDataSet
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: FragmentNoticeListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Notice, listener: OnItemClickListener?) {
            binding.notice = data

            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(it, position, data.url)
                    binding.tvNotice.apply {
                        isSelected = !isSelected
                    }
                    Log.d("MarqueeTest", "isSelected=${binding.tvNotice.isSelected}")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            FragmentNoticeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position], itemClickListener)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}