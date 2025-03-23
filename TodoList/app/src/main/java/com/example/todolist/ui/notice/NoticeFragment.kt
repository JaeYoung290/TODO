package com.example.todolist.ui.notice

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.notice.repository.WebPageRepositoryImpl
import com.example.domain.notice.repository.NoticeRepository
import com.example.domain.notice.repository.WebPageRepository
import com.example.todolist.databinding.FragmentNoticeBinding
import com.example.todolist.ui.notice.adapter.NoticeRecyclerAdapter
import com.example.todolist.ui.notice.adapter.SwipeToDeleteCallback
import com.example.todolist.ui.notice.viewmodel.NoticeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

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

            val swipeHandler = SwipeToDeleteCallback(requireContext(), adapter as NoticeRecyclerAdapter, viewModel)
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
                    "favorites" -> viewModel.getFavoriteItem()
                    "hidden" -> viewModel.getDeletedItem()
                    else -> viewModel.getNoticeByCategory(category)
                }
            }
        }
    }
}