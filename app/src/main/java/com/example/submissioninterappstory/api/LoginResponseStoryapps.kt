package com.example.submissioninterappstory.api

import com.google.gson.annotations.SerializedName

data class LoginResponseStoryapps(
    @field:SerializedName("loginResult")
    val logInakun: loginResult,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class loginResult(
    @field: SerializedName ("name")
    val name: String,

    @field: SerializedName ("userId")
    val userId: String,

    @field: SerializedName ("token")
    val token: String,
)