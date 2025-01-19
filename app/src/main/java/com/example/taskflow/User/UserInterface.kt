package com.example.taskflow.User

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserInterface {
    @POST("register/")
    fun register(@Body user: UserRegister): Call<UserRegister>

    @POST("login/")
    fun login(@Body user: UserLogin): Call<LoginResponse>

    @GET("users/me/")
    fun getUserData(
        @Header("Authorization") token: String
    ): Call<UserLogin>
}