package com.example.domain.naver.calendar

import com.example.domain.naver.calendar.repository.NaverApiRepository
import javax.inject.Inject

class NaverApiUseCase @Inject constructor (
    private val naverApiRepository: NaverApiRepository
) {
    suspend fun getNaverUserNickname(accessToken : String) : String {
        return naverApiRepository.getUserNickname(accessToken)
    }
    suspend fun addNaverCalendarSchedule (
        accessToken : String,
        title : String,
        detail : String,
        startDate : String,
        endDate : String,
        startTime : String,
        endTime : String
    ) : Boolean {
        return  naverApiRepository.postCalendar(accessToken, title, detail, startDate, endDate, startTime, endTime)
    }
}