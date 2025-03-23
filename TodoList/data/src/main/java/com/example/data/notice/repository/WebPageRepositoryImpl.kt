package com.example.data.notice.repository

import android.content.Context
import android.util.Log
import com.example.data.notice.source.notice.NoticeDao
import com.example.data.notice.source.notice.NoticeEntity
import com.example.domain.notice.model.Notice
import com.example.domain.notice.repository.WebPageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WebPageRepositoryImpl(private val noticeDao: NoticeDao) : WebPageRepository {
    override suspend fun parseWebPages() {
        val urls = listOf(
            "https://www.koreatech.ac.kr/notice/list.es?mid=a10604010000&board_id=14" to "general",
            "https://www.koreatech.ac.kr/notice/list.es?mid=a10604020000&board_id=15" to "scholarship",
            "https://www.koreatech.ac.kr/notice/list.es?mid=a10604030000&board_id=16" to "academic",
            "https://www.koreatech.ac.kr/notice/list.es?mid=a10604040000&board_id=150" to "employment"
        )

        for ((url, category) in urls) {
            val baseUrl = "https://www.koreatech.ac.kr"
            try {
                withContext(Dispatchers.IO) {
                    val document = Jsoup.connect(url).get()
                    val titles = document.select("td.txt_left span")
                    val noticeUrl = document.select("td.txt_left a")
                    val dates = document.select("td[aria-label=등록일]")

                    for (i in titles.indices) {
                        val title = titles[i].ownText()
                        val href = URL(URL(baseUrl), noticeUrl[i].attr("href")).toString()
                        val date = dates[i].ownText()

                        val noticeEntity = NoticeEntity(
                            title = title,
                            url = href,
                            date = date,
                            category = category
                        )
                        noticeDao.insert(noticeEntity)
                    }
                }
            } catch (e: Exception) {
                Log.d("WebPageRepositoryImpl", "데이터 파싱 실패")
                e.printStackTrace()
            }
        }
        Log.d("데이터베이스", "${noticeDao.getItemsByCategory("general")}")
        Log.d("데이터베이스", "${noticeDao.getItemsByCategory("scholarship")}")
        Log.d("데이터베이스", "${noticeDao.getItemsByCategory("academic")}")
        Log.d("데이터베이스", "${noticeDao.getItemsByCategory("employment")}")
    }
}