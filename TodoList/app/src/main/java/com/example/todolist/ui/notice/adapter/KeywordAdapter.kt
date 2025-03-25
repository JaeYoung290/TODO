package com.example.todolist.ui.notice.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.notice.model.Keyword
import com.example.todolist.databinding.FragmentKeywordDialogListItemBinding

class KeywordAdapter(private val keywords: MutableList<Keyword>) : RecyclerView.Adapter<KeywordAdapter.KeywordViewHolder>() {

    inner class KeywordViewHolder(private val binding: FragmentKeywordDialogListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(keyword: Keyword) {
            binding.textViewKeyword.text = keyword.keyword
            binding.imageButtonDelete.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    keywords.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        val binding = FragmentKeywordDialogListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KeywordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        holder.bind(keywords[position])
    }

    override fun getItemCount() = keywords.size

    fun updateData(newKeywords: List<Keyword>) {
        keywords.clear()
        keywords.addAll(newKeywords)
        notifyDataSetChanged()
    }
}
