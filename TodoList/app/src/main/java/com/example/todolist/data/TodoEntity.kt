package com.example.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val no: Int = 0,
    val date: Int,
    val checked: Boolean,
    var todo: String
)
