package com.example.data.naver.calendar.response.calendar

import com.google.gson.annotations.SerializedName

data class NaverCalendarResponse (
    @SerializedName("result") val result: String?,
){
}