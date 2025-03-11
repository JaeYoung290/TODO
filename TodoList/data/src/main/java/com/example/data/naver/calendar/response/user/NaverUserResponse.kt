package com.example.data.naver.calendar.response.user

import com.google.gson.annotations.SerializedName

data class NaverUserResponse (
    @SerializedName("resultcode") val resultCode: String,
    @SerializedName("message") val message: String,
    @SerializedName("response") val response: NaverUserProfile?
)

data class NaverUserProfile(
    @SerializedName("id") val id: String?,
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("profile_image") val profileImage: String?,
    @SerializedName("age") val age: String?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("birthday") val birthday: String?
)