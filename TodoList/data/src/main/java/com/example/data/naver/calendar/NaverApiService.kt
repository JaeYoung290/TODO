package com.example.data.naver.calendar

import com.example.data.naver.calendar.response.calendar.NaverCalendarResponse
import com.example.data.naver.calendar.response.user.NaverUserResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface NaverApiService {
    @GET("v1/nid/me")
    suspend fun getUserProfile(
        @Header("Authorization") accessToken: String
    ): NaverUserResponse

    @FormUrlEncoded
    @POST("calendar/createSchedule.json")
    suspend fun addEvent(
        @Header("Authorization") accessToken: String,
        @Field("calendarId") calendarId: String = "defaultCalendarId",
        @Field("scheduleIcalString") icalString: String
    ): NaverCalendarResponse
}