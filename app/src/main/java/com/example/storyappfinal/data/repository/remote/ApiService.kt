package com.example.storyappfinal.data.repository.remote

import com.example.storyappfinal.data.model.Login
import com.example.storyappfinal.data.model.Register
import com.example.storyappfinal.data.model.StoryList
import com.example.storyappfinal.data.model.StoryUpload
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("login")
    @FormUrlEncoded
    fun doLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Login>

    @POST("register")
    @FormUrlEncoded
    fun doRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Register>

    @GET("stories")
    suspend fun getStoryList(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryList

    @GET("stories?location=1")
    fun getStoryListLocation(
        @Header("Authorization") token: String,
        @Query("size") size: Int
    ): Call<StoryList>

    @Multipart
    @POST("stories")
    fun doUploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<StoryUpload>

}