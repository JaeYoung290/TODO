package com.example.todolist.ui.main

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.todolist.R
import com.example.todolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavi.setupWithNavController(findNavController(R.id.navi_host_fragment))
        setBottomNaviColor()
    }

    private fun setBottomNaviColor() {
        binding.bottomNavi.itemActiveIndicatorColor = ColorStateList.valueOf(getColor(R.color.violet_162))
        binding.bottomNavi.itemIconTintList = null
    }
}