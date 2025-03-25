package com.example.todolist.ui.notice

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.notice.model.Keyword
import com.example.todolist.databinding.FragmentNoticeDialogBinding
import com.example.todolist.ui.notice.adapter.KeywordAdapter
import com.example.todolist.ui.notice.viewmodel.NoticeViewModel
import kotlinx.coroutines.launch


class KeywordDialogFragment : DialogFragment() {

    private var _binding: FragmentNoticeDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NoticeViewModel
    private lateinit var adapter: KeywordAdapter
    private val keywords = mutableListOf<Keyword>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[NoticeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoticeDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextInnerKeyword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL

        binding.recyclerViewKeywords.layoutManager = LinearLayoutManager(context)
        adapter = KeywordAdapter(keywords)
        binding.recyclerViewKeywords.adapter = adapter

        viewModel.keywordList.observe(viewLifecycleOwner) { keywords ->
            this.keywords.clear()
            this.keywords.addAll(keywords)
            adapter.updateData(this.keywords.toList())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchKeywords()
        }

        binding.imageButtonAddKeyword.setOnClickListener {
            val keywordText = binding.editTextInnerKeyword.text.toString().trim()
            if (keywordText.isNotEmpty()) {
                val newKeyword = Keyword(0, keywordText)
                keywords.add(newKeyword)
                adapter.notifyItemInserted(keywords.size - 1)
                binding.editTextInnerKeyword.text.clear()
            }
        }

        binding.buttonDialogPositive.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                keywords.forEach { keyword ->
                    if (keyword.id == 0L) {
                        viewModel.insertKeyword(keyword)
                    } else {
                        viewModel.keywordUseCases.deleteKeywordById(keyword.id.toInt())
                    }
                }
            }
            dismiss()
        }

        binding.buttonDialogNegative.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchKeywords()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}