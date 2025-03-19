package com.example.data


class TodoRepository(private val todoDao: TodoDao) {
    suspend fun getTodosByDate(date: Int) = todoDao.getTodosByDate(date)

    suspend fun getTodosByDateAndChecked(date: Int, checked: Boolean) =
        todoDao.getTodosByDateAndChecked(date, checked)

    suspend fun deleteTodosByDate(date: Int) = todoDao.deleteTodosByDate(date)

    suspend fun updateTodo(todo: TodoEntity) = todoDao.updateTodo(todo)

    suspend fun insertTodo(todo: TodoEntity) = todoDao.insertTodo(todo)

    suspend fun insertTodos(todos: List<TodoEntity>) = todoDao.insertTodos(todos)

    suspend fun deleteAllTodos() = todoDao.deleteAll()
}
