package com.example.data.todo

import androidx.room.*
import com.example.domain.todo.TodoEntity

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo_table ORDER BY date ASC")
    suspend fun getTodosAll(): List<TodoEntity>

    @Query("SELECT * FROM todo_table WHERE date = :date")
    suspend fun getTodosByDate(date: Int): List<TodoEntity>

    @Query("SELECT * FROM todo_table WHERE date = :date AND checked = :checked")
    suspend fun getTodosByDateAndChecked(date: Int, checked: Boolean): List<TodoEntity>

    @Query("DELETE FROM todo_table WHERE date = :date")
    suspend fun deleteTodosByDate(date: Int)

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodos(todos: List<TodoEntity>)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAll()

}
