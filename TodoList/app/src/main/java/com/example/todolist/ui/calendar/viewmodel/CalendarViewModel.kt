package com.example.todolist.ui.calendar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.naver.calendar.NaverApiUseCase
import com.example.domain.todo.model.Todo
import com.example.todolist.ui.main.viewmodel.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val naverApiUseCase: NaverApiUseCase
) : ViewModel() {
    var todoList : MutableList<Todo> = mutableListOf()
    var userName = MutableLiveData<String>("---")

    fun getNaverUserName() {
        val accessToken = MainViewModel.NaverLoginData.accessToken
        if(accessToken != null) {
            viewModelScope.launch {
                MainViewModel.NaverLoginData.userName = naverApiUseCase.getNaverUserNickname(accessToken)
                userName.value = MainViewModel.NaverLoginData.userName
            }
        }else {
            Log.e("MY_TAG","error null accessToken")
        }
    }
}