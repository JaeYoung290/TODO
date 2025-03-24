package com.example.domain.notice.model

data class Notice(
    val id: Long,
    val title: String,
    val url: String,
    val date: String,
    val category: String,
    var isFavorite: Boolean = false,
    var isDeleted: Boolean = false,
)
