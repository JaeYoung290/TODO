package com.example.domain.notice.repository

import android.content.Context

interface WebPageRepository {

    suspend fun parseWebPages()
}