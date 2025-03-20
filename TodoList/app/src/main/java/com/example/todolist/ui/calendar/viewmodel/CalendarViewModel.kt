package com.example.todolist.ui.calendar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.TodoEntity
import com.example.data.TodoRepository
import com.example.domain.naver.calendar.NaverApiUseCase
import com.example.todolist.ui.main.viewmodel.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val naverApiUseCase: NaverApiUseCase,
    private val todoRepository: TodoRepository
) : ViewModel() {
    var todoList : MutableList<TodoEntity> = mutableListOf()
    var userName = MutableLiveData<String>("---")
    var addScheduleSuccess = MutableLiveData<Boolean?>(null)

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

    fun addNaverCalendarSchedule(
        title : String,
        detail : String,
        startDate : String,
        endDate : String,
        startTime : String,
        endTime : String
    ) {
        val accessToken = MainViewModel.NaverLoginData.accessToken
        if(accessToken != null) {
            viewModelScope.launch {
                addScheduleSuccess.value = naverApiUseCase.addNaverCalendarSchedule(
                    accessToken,
                    title,
                    detail,
                    startDate,
                    endDate,
                    startTime,
                    endTime
                )
            }
        }else {
            Log.e("MY_TAG","error null accessToken")
        }
    }

    fun setTodoList() {
        viewModelScope.launch {
            todoList.addAll(todoRepository.getTodosAll())
        }
    }
}