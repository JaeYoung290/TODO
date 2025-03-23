package com.example.todolist.ui.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.FragmentNoticeBinding
import com.example.todolist.ui.notice.adapter.NoticeRecyclerAdapter
import com.example.todolist.ui.notice.adapter.SwipeToDeleteCallback
import com.example.todolist.ui.notice.viewmodel.NoticeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoticeFragment : Fragment() {

    private lateinit var binding: FragmentNoticeBinding
    private val viewModel: NoticeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoticeBinding.inflate(inflater, container, false)
        binding.btnGeneralNotice.isSelected = true

        setupRecyclerView()
        setupButtonListeners()
        observeNoticeList()
        viewModel.fetchNotices()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvNotice.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = NoticeRecyclerAdapter(emptyList(), viewModel)

            val swipeHandler =
                SwipeToDeleteCallback(requireContext(), adapter as NoticeRecyclerAdapter, viewModel)
            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(this)
        }
    }

    private fun observeNoticeList() {
        viewModel.noticeList.observe(viewLifecycleOwner) { notices ->
            (binding.rvNotice.adapter as? NoticeRecyclerAdapter)?.updateNotice(notices)
        }
    }

    private fun setupButtonListeners() {
        val categoryButtons = listOf(
            binding.btnGeneralNotice to "general",
            binding.btnScholarshipNotice to "scholarship",
            binding.btnAcademicNotice to "academic",
            binding.btnEmploymentNotice to "employment"
        )

        val allButtons = categoryButtons + listOf(
            binding.btnFavoritesNotice to "favorites",
            binding.btnHiddenNotice to "hidden"
        )

        allButtons.forEach { (button, category) ->
            button.setOnClickListener {
                allButtons.forEach { (btn, _) ->
                    btn.isSelected = false
                }
                button.isSelected = true

                when (category) {
                    "favorites", "hidden" -> {
                        binding.btnFiltering.isEnabled = false
                        binding.btnFiltering.isChecked = false
                        binding.btnFiltering.isClickable = false
                    }

                    else -> {
                        binding.btnFiltering.isEnabled = true
                        binding.btnFiltering.isClickable = true
                    }
                }

                when (category) {
                    "favorites" -> viewModel.getFavoriteItem()
                    "hidden" -> viewModel.getDeletedItem()
                    else -> {
                        if (binding.btnFiltering.isChecked) {
                            viewModel.getItemsByKeywords(category)
                        } else {
                            viewModel.getNoticeByCategory(category)
                        }
                    }
                }
            }
        }

        binding.btnFiltering.setOnCheckedChangeListener { _, isChecked ->
            val selectedCategory = allButtons.find { it.first.isSelected }?.second ?: "general"

            if (isChecked) {
                viewModel.getItemsByKeywords(selectedCategory)
            } else {
                viewModel.getNoticeByCategory(selectedCategory)
            }
        }

        binding.ibSort.setOnClickListener {
            binding.ibSort.isSelected = !binding.ibSort.isSelected

            if (binding.ibSort.isSelected) {
                binding.sortOptionsLayout.visibility = View.VISIBLE
            } else {
                binding.sortOptionsLayout.visibility = View.GONE
            }
        }
    }
}