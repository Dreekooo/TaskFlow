package com.example.taskflow.addProject

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProjectInterface {
    @GET("projects/")
    suspend fun getProjects(): List<Project>

    @GET("projects/{id}/")
    suspend fun getProject(@Path("id") id: Int): Project

    @DELETE("projects/{id}/")
    fun deleteProject(@Path("id") id: Int): Call<Void>

    @PUT("projects/{id}/")
    fun updateProject(
        @Path("id") id: Int,
        @Body projectPost: ProjectPost
    ): Call<Project>
}