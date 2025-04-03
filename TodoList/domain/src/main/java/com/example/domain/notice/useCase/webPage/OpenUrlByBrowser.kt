package com.example.domain.notice.useCase.webPage

import android.content.Context
import android.content.Intent
import android.net.Uri
import javax.inject.Inject

class OpenUrlByBrowser @Inject constructor(private val context: Context) {
    fun openUrlByBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}