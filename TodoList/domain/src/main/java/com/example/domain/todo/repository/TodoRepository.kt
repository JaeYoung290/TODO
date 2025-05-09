package com.example.domain.todo.repository

import com.example.domain.todo.TodoEntity

interface TodoRepository {
    suspend fun getTodosAll(): List<TodoEntity>
    suspend fun getTodosByDate(date: Int): List<TodoEntity>
    suspend fun getTodosByDateAndChecked(date: Int, checked: Boolean): List<TodoEntity>
    suspend fun deleteTodosByDate(date: Int)
    suspend fun updateTodo(todo: TodoEntity)
    suspend fun insertTodo(todo: TodoEntity)
    suspend fun insertTodos(todos: List<TodoEntity>)
    suspend fun deleteAllTodos()
}
