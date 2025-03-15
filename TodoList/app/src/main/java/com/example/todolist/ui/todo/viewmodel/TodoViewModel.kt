package com.example.todolist.ui.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.TodoEntity
import com.example.todolist.data.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {

    fun getTodosByDate(date: Int, callback: (List<TodoEntity>) -> Unit) {
        viewModelScope.launch {
            callback(repository.getTodosByDate(date))
        }
    }

    fun getTodosByDateAndChecked(date: Int, checked: Boolean, callback: (List<TodoEntity>) -> Unit) {
        viewModelScope.launch {
            callback(repository.getTodosByDateAndChecked(date, checked))
        }
    }

    fun deleteTodosByDate(date: Int) {
        viewModelScope.launch {
            repository.deleteTodosByDate(date)
        }
    }

    fun updateTodo(no: Int, date: Int, checked: Boolean, todo: String) {
        viewModelScope.launch {
            repository.updateTodo(TodoEntity(no, date, checked, todo))
        }
    }

    fun insertTodo(date: Int, checked: Boolean, todo: String) {
        viewModelScope.launch {
            repository.insertTodo(TodoEntity(date = date, checked = checked, todo = todo))
        }
    }

    fun insertTodos(todos: List<TodoEntity>) {
        viewModelScope.launch {
            repository.insertTodos(todos)
        }
    }

    fun deleteAllTodos() {
        viewModelScope.launch {
            repository.deleteAllTodos()
        }
    }
}
