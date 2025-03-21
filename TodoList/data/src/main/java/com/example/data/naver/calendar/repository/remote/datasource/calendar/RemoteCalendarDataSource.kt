package com.example.data.naver.calendar.repository.remote.datasource.calendar

interface RemoteCalendarDataSource {
    suspend fun postCalendar(
        accessToken : String,
        title : String,
        detail : String,
        startDate : String,
        endDate : String,
        startTime : String,
        endTime : String
    ): Boolean
}