package com.example.domain.notice.useCase.webPage

import android.content.Context
import android.content.Intent
import android.net.Uri

class OpenUrlByBrowser {
    fun openUrlByBrowser(url: String, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}