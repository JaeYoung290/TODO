package com.example.data.todo

import com.example.domain.todo.repository.TodoRepository
import com.example.domain.todo.TodoEntity

class TodoRepositoryImpl(private val todoDao: TodoDao) : TodoRepository {
    override suspend fun getTodosAll() = todoDao.getTodosAll()
    override suspend fun getTodosByDate(date: Int) = todoDao.getTodosByDate(date)
    override suspend fun getTodosByDateAndChecked(date: Int, checked: Boolean) = todoDao.getTodosByDateAndChecked(date, checked)
    override suspend fun deleteTodosByDate(date: Int) = todoDao.deleteTodosByDate(date)
    override suspend fun updateTodo(todo: TodoEntity) = todoDao.updateTodo(todo)
    override suspend fun insertTodo(todo: TodoEntity) = todoDao.insertTodo(todo)
    override suspend fun insertTodos(todos: List<TodoEntity>) = todoDao.insertTodos(todos)
    override suspend fun deleteAllTodos() = todoDao.deleteAll()
}
