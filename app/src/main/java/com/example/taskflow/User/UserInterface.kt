package com.example.taskflow.User

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserInterface {
    @POST("register/")
    fun register(@Body user: UserRegister): Call<UserRegister>

    @POST("login/")
    fun login(@Body user: UserLogin): Call<LoginResponse>
}