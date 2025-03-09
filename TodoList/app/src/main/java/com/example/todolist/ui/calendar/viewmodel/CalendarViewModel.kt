package com.example.todolist.ui.calendar.viewmodel

import androidx.lifecycle.ViewModel
import com.example.domain.todo.model.Todo

class CalendarViewModel () : ViewModel() {
    var todoList : MutableList<Todo> = mutableListOf()
}