package com.example.domain.naver.calendar.repository

interface NaverApiRepository {
    suspend fun getUserNickname(accessToken : String) : String
    suspend fun postCalendar(
        accessToken : String,
        title : String,
        detail : String,
        startDate : String,
        endDate : String,
        startTime : String,
        endTime : String
    ) : Boolean
}