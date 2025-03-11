package com.example.data.naver.calendar.repository

interface NaverApiRepository {
    suspend fun getUserNickname(accessToken : String) : String
    suspend fun postCalendar(accessToken : String) : String
}