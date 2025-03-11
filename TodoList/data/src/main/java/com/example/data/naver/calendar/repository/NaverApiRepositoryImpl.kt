package com.example.data.naver.calendar.repository

import android.util.Log
import com.example.data.naver.calendar.NaverApiClient

class NaverApiRepositoryImpl () : NaverApiRepository {

    override suspend fun getUserNickname(accessToken : String) : String {
        try {
            val response = NaverApiClient.apiService.getUserProfile("Bearer $accessToken")
            return response.response?.nickname ?: "empty"
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("MY_TAG","error exception ${e}")
            return "error"
        }
    }

    override suspend fun postCalendar(accessToken : String): String {
        return ""
    }
}