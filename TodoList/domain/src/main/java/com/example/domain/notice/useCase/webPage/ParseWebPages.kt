package com.example.domain.notice.useCase.webPage

import com.example.domain.notice.repository.WebPageRepository

class ParseWebPages(private val repository: WebPageRepository) {
    suspend operator fun invoke() {
        repository.parseWebPages()
    }
}