package com.example.todolist.ui.main.viewmodel

import androidx.lifecycle.ViewModel

class MainViewModel () : ViewModel() {
    object NaverLoginData {
        var accessToken : String? = null
        var refreshToken : String? = null
        var userName : String? = null
    }
}