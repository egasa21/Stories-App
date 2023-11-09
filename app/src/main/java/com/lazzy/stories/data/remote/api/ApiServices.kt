package com.lazzy.stories.data.remote.api

import com.lazzy.stories.data.remote.LoginBody
import com.lazzy.stories.data.remote.RegisterBody
import com.lazzy.stories.data.remote.response.AddStoryResponse
import com.lazzy.stories.data.remote.response.AuthResponse
import com.lazzy.stories.data.remote.response.DetailStoriesResponse
import com.lazzy.stories.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @POST("register")
    fun registerUser(
        @Body registerBody: RegisterBody
    ): Call<AuthResponse>

    @POST("login")
    fun loginUser(
        @Body loginBody: LoginBody
    ): Call<AuthResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryResponse

    @GET("stories/{id}")
    fun getDetailStories(
        @Path("id") id: String
    ): Call<DetailStoriesResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<AddStoryResponse>
}