package com.example.todolist.ui.notice.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.notice.model.Notice
import com.example.domain.notice.useCase.database.DatabaseUseCases
import com.example.domain.notice.useCase.webPage.WebPageUseCases
import com.example.todolist.ui.notice.adapter.NoticeRecyclerAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val webPageUseCases: WebPageUseCases,
    private val databaseUseCases: DatabaseUseCases
) : ViewModel() {

    private val _noticeList = MutableLiveData<List<Notice>>()
    val noticeList: LiveData<List<Notice>> = _noticeList

    private val _favoriteNotice = MutableLiveData(false)
    val favoriteNotice: LiveData<Boolean> = _favoriteNotice

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
            } catch (e: Exception) {
                Log.e("NoticeViewModel", "Error(NoticeViewModel): ${e.message}")
            }
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

    private fun favoriteNoticeById(itemId: Int, isFavorite: Boolean) {
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
}