package com.example.submissioninterappstory.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiUserInter {

    @FormUrlEncoded
    @POST("/v1/register")
    fun regisakunAppstories(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ApiInterResponse>

    @FormUrlEncoded
    @POST("/v1/login")
    fun logInakunAppstories(
        @Field("email") email: String,
        @Field("password") passwword: String
    ): Call<LoginResponseStoryapps>

    @Multipart
    @POST("/v1/stories")
    fun uploadImageakunstory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): Call<ApiInterResponse>

    @GET("/v1/stories")
    fun getAllStoriesakun(
        @Header("Authorization") token: String
    ): Call<StoryAllRespon>

    @GET("/v1/stories")
    fun getAllStoryLocation(
        @Header("Authorization") token: String,
        @Query ("location") location: Int = 1
    ): Call<StoryAllRespon>

    @GET("/v1/stories")
    suspend fun getStories(
        @Header("Authorization") Token: String,
        @Query("page") page: Int?,
        @Query("size") size : Int?
    ): StoryAllRespon
}