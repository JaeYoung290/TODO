package com.example.todolist.ui.calendar.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.naver.calendar.NaverApiUseCase
import com.example.domain.todo.TodoEntity
import com.example.domain.todo.repository.TodoRepository
import com.example.todolist.ui.main.viewmodel.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
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
    var setTodoListSuccess = MutableLiveData<Boolean?>(null)
        var toggleASC = true

    fun getNaverUserName() {
        val accessToken = MainViewModel.NaverLoginData.accessToken
        if(accessToken != null) {
            viewModelScope.launch(Dispatchers.IO) {
                MainViewModel.NaverLoginData.userName = naverApiUseCase.getNaverUserNickname(accessToken)
                userName.postValue(MainViewModel.NaverLoginData.userName)
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
            viewModelScope.launch(Dispatchers.IO) {
                addScheduleSuccess.postValue(naverApiUseCase.addNaverCalendarSchedule(
                    accessToken,
                    title,
                    detail,
                    startDate,
                    endDate,
                    startTime,
                    endTime
                ))
            }
        }else {
            Log.e("MY_TAG","error null accessToken")
        }
    }

    fun setTodoList(keyword: String = "") {
        todoList = mutableListOf()
        viewModelScope.launch(Dispatchers.IO) {
            if(toggleASC) {
                if(keyword == "") {
                    setTodoListSuccess.postValue(todoList.addAll(todoRepository.getTodosAllASC()))
                }else {
                    setTodoListSuccess.postValue(todoList.addAll(todoRepository.getTodosByKeywordASC(keyword)))
                }
            }else {
                if(keyword == "") {
                    setTodoListSuccess.postValue(todoList.addAll(todoRepository.getTodosAllDESC()))
                }else {
                    setTodoListSuccess.postValue(todoList.addAll(todoRepository.getTodosByKeywordDESC(keyword)))
                }
            }
        }
    }

        fun searchEtDebounce (flow : Flow<String>) {
            viewModelScope.launch {
                flow
                    .debounce(500) // 500ms 동안 입력이 없으면 실행 (aplly 내에서 실행 불가)
                    .distinctUntilChanged()
                    .collect { text ->
                        setTodoList(text)
                    }
            }
        }
}