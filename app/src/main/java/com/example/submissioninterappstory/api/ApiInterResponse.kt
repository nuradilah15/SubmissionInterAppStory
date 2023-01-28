package com.example.submissioninterappstory.api

import com.google.gson.annotations.SerializedName

data class ApiInterResponse (
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)