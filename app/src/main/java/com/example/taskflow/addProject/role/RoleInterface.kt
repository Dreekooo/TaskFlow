package com.example.taskflow.addProject.role

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RoleInterface {
    @POST("roles/")
    fun addRole(@Body role: RolePost): Call<Void>

    @GET("roles/")
    suspend fun getRole(): List<Role>
}