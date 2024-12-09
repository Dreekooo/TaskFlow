package com.example.taskflow.addProject.users

import retrofit2.http.GET
import retrofit2.http.Path

interface UserInterface {
    @GET("users/{id}/")
    suspend fun getUser(@Path("id") id: Int): User

    @GET("users/")
    suspend fun getAllUsers(): List<User>
}