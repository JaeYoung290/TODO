package com.example.data.naver.calendar.repository.remote.datasource.user

interface RemoteUserDataSource {
    suspend fun getUserNickname(accessToken : String) : String
}