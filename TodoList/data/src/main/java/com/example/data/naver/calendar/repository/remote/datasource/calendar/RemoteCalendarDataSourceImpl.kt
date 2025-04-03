package com.example.data.naver.calendar.repository.remote.datasource.calendar

import com.example.data.naver.calendar.NaverApiClient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class RemoteCalendarDataSourceImpl () : RemoteCalendarDataSource {
    override suspend fun postCalendar(
        accessToken : String,
        title : String,
        detail : String,
        startDate : String,
        endDate : String,
        startTime : String,
        endTime : String
    ): Boolean {
        val localDateTime = LocalDateTime.now()
        val formatterDate = DateTimeFormatter.ofPattern("yyyyMMdd")
        val formatterTime = DateTimeFormatter.ofPattern("HHmmss")
        val localDate = localDateTime.format(formatterDate)
        val localTime = localDateTime.format(formatterTime)
        var uid = UUID.randomUUID().toString()
        val scheduleIcalString = """
BEGIN:VCALENDAR
VERSION:2.0
PRODID:Naver Calendar
CALSCALE:GREGORIAN
BEGIN:VTIMEZONE
TZID:Asia/Seoul
BEGIN:STANDARD
DTSTART:19700101T000000
TZNAME:GMT+09:00
TZOFFSETFROM:+0900
TZOFFSETTO:+0900
END:STANDARD
END:VTIMEZONE
BEGIN:VEVENT
SEQUENCE:0
CLASS:PUBLIC
TRANSP:OPAQUE
UID:$uid
DTSTART;TZID=Asia/Seoul:${startDate}T${startTime}
DTEND;TZID=Asia/Seoul:${endDate}T${endTime}
SUMMARY:$title
DESCRIPTION:$detail
CREATED:${localDate}T${localTime}Z
LAST-MODIFIED:${localDate}T${localTime}Z
DTSTAMP:${localDate}T${localTime}Z
END:VEVENT
END:VCALENDAR
""".trimIndent()
        //val encodedIcalString = URLEncoder.encode(scheduleIcalString, "UTF-8") retrofit 은 자동 encode
        val response = NaverApiClient.apiService
            .addEvent("Bearer $accessToken", "defaultCalendarId", scheduleIcalString)

        return response.result == "success"
    }
}