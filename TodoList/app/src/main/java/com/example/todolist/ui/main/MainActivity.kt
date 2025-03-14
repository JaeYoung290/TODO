package com.example.todolist.ui.main

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.todolist.R
import com.example.todolist.databinding.ActivityMainBinding
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavi(getColor(R.color.violet_162))
    }

    // Bottom navigation 초기 설정
    private fun setBottomNavi(color : Int) {
        val navController = findNavController(R.id.navi_host_fragment)
        binding.bottomNavi.apply {
            //setupWithNavController(navController) 자동 추가 방식 (단 따로 onItemSelected 를 사용할 수 없음)
            itemActiveIndicatorColor = ColorStateList.valueOf(color)
            itemIconTintList = null
        }

        binding.bottomNavi.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navi_notice -> {
                    navController.navigate(R.id.navi_notice) // 수동 방식
                    binding.textViewTitle.text = getString(R.string.fragment_title_notice)
                }
                R.id.navi_todo -> {
                    navController.navigate(R.id.navi_todo)
                    binding.textViewTitle.text = getString(R.string.fragment_title_todo)
                }
                R.id.navi_calendar -> {
                    navController.navigate(R.id.navi_calendar)
                    binding.textViewTitle.text = getString(R.string.fragment_title_calendar)
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    fun setBottomNavVisibility(visible: Boolean) {
        // 키보드 올라오면 bottomNavi가 화면을 너무 가려서 잠시 숨김
        binding.bottomNavi.visibility = if (visible) View.VISIBLE else View.GONE
    }
}