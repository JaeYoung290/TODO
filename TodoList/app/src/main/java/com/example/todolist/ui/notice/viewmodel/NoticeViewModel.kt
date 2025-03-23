package com.example.todolist.ui.notice.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.notice.model.Keyword
import com.example.domain.notice.model.Notice
import com.example.domain.notice.model.SortOption
import com.example.domain.notice.useCase.core.CoreUseCases
import com.example.domain.notice.useCase.database.keyword.KeywordUseCases
import com.example.domain.notice.useCase.database.notice.DatabaseUseCases
import com.example.domain.notice.useCase.webPage.OpenUrlByBrowser
import com.example.domain.notice.useCase.webPage.WebPageUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.security.Key
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val webPageUseCases: WebPageUseCases,
    private val databaseUseCases: DatabaseUseCases,
    private val openUrlByBrowser: OpenUrlByBrowser,
    private val keywordUseCases: KeywordUseCases,
    private val coreUseCases: CoreUseCases
) : ViewModel() {

    private val _noticeList = MutableLiveData<List<Notice>>()
    val noticeList: LiveData<List<Notice>> = _noticeList

    private val _favoriteNotice = MutableLiveData(false)
    val favoriteNotice: LiveData<Boolean> = _favoriteNotice

    private val _keywordList = MutableLiveData<List<Keyword>>()
    val keywordList: LiveData<List<Keyword>> = _keywordList

    fun fetchNotices() {
        viewModelScope.launch {
            webPageUseCases.parseWebPages()
            getNoticeByCategory("general")
        }
    }

    fun getNoticeByCategory(category: String) {
        viewModelScope.launch {
            try {
                val notice = databaseUseCases.getNoticeByCategory(category)
                _noticeList.value = notice
                _favoriteNotice.value = false
                Log.d("데이터", notice.toString())
            } catch (e: Exception) {
                Log.e("NoticeViewModel", "Error(NoticeViewModel): ${e.message}")
            }
        }
    }

    fun sortListBy(sortBy: SortOption, isAsc: Boolean): String {
        return coreUseCases.sortListUseCase.getSortByString(sortBy, isAsc)
    }

    fun getItemsByKeywords(category: String) {
        viewModelScope.launch {
            val keywordList = keywordUseCases.getAllKeywords()
            val keywordStringList = keywordList.map { it.keyword }
            val result = databaseUseCases.getItemsByKeywords(category, keywordStringList)
            _noticeList.value = result
        }
    }

    fun deleteNoticeById(itemId: Int, isDeleted: Boolean) {
        viewModelScope.launch {
            try {
                databaseUseCases.deleteNotice(itemId, isDeleted)
                _noticeList.value = _noticeList.value?.filter { it.id.toInt() != itemId }
            } catch (e: Exception) {
                Log.e("NoticeViewModel", "Error(Delete Failed): ${e.message}")
            }
        }
    }

    fun favoriteNoticeById(itemId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                databaseUseCases.favoriteNotice(itemId, isFavorite)
                if (_favoriteNotice.value == true && !isFavorite) {
                    _noticeList.value = _noticeList.value?.filter { it.id.toInt() != itemId }
                } else {
                    _noticeList.value = _noticeList.value?.map { notice ->
                        if (notice.id.toInt() == itemId) notice.copy(isFavorite = isFavorite) else notice
                    }
                }
            } catch (e: Exception) {
                Log.e("NoticeViewModel", "Error(Favorite State Change Failed): ${e.message}")
            }
        }
    }

    fun onFavoriteClicked(notice: Notice) {
        val favoriteState = !notice.isFavorite
        favoriteNoticeById(notice.id.toInt(), favoriteState)
    }

    fun getDeletedItem() {
        viewModelScope.launch {
            try {
                val notice = databaseUseCases.getDeletedItem()
                _noticeList.value = notice
            } catch (e: Exception) {
                Log.e("NoticeViewModel", "Error(Delete Failed): ${e.message}")
            }
        }
    }

    fun getFavoriteItem() {
        viewModelScope.launch {
            try {
                val notice = databaseUseCases.getFavoriteItem()
                _noticeList.value = notice
                _favoriteNotice.value = true
            } catch (e: Exception) {
                Log.e("NoticeViewModel", "Error(Delete Failed): ${e.message}")
            }
        }
    }

    fun onItemClicked(notice: Notice) {
        openUrlByBrowser.openUrlByBrowser(notice.url)
    }
}