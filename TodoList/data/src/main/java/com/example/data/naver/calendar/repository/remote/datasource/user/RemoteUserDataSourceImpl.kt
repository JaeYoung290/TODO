package com.example.data.naver.calendar.repository.remote.datasource.user

import com.example.data.naver.calendar.NaverApiClient

class RemoteUserDataSourceImpl () : RemoteUserDataSource {
    override suspend fun getUserNickname(accessToken : String) : String {
        val response = NaverApiClient.apiService.getUserProfile("Bearer $accessToken")
        return response.response?.nickname ?: "empty"
    }
}