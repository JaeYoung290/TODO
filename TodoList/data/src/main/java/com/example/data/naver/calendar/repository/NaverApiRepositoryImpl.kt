package com.example.data.naver.calendar.repository

import android.util.Log
import com.example.data.naver.calendar.repository.remote.datasource.calendar.RemoteCalendarDataSource
import com.example.data.naver.calendar.repository.remote.datasource.user.RemoteUserDataSource
import com.example.domain.naver.calendar.repository.NaverApiRepository
import javax.inject.Inject

class NaverApiRepositoryImpl @Inject constructor(
    private val remoteUserDataSource: RemoteUserDataSource,
    private val remoteCalendarDataSource: RemoteCalendarDataSource
) : NaverApiRepository {

    override suspend fun getUserNickname(accessToken : String) : String {
        try {
            return remoteUserDataSource.getUserNickname(accessToken)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("NAVER_GET_USER_INFO","Error exception ${e}")
            return "error"
        }
    }

    override suspend fun postCalendar(
        accessToken : String,
        title : String,
        detail : String,
        startDate : String,
        endDate : String,
        startTime : String,
        endTime : String
    ): Boolean {
        try {
            if(remoteCalendarDataSource.postCalendar(accessToken,title,detail,startDate,endDate,startTime,endTime)) {
                Log.e("NAVER_ADD_EVENT","Success addCalendar")
                return true
            }else {
                Log.e("NAVER_ADD_EVENT","Fail addCalendar")
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("NAVER_ADD_EVENT","Error exception ${e}")
            return false
        }
    }
}