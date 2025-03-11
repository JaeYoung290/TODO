package com.example.domain.naver.calendar

import com.example.data.naver.calendar.repository.NaverApiRepository
import javax.inject.Inject

class NaverApiUseCase @Inject constructor (
    private val naverApiRepository: NaverApiRepository
) {
    suspend fun getNaverUserNickname(accessToken : String) : String {
        return naverApiRepository.getUserNickname(accessToken)
    }

}