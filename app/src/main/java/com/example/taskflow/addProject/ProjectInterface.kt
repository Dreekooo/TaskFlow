package com.example.taskflow.addProject

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface ProjectInterface {
    @GET("projects/")
    suspend fun getProjects(): List<Project>

    @DELETE("projects/{id}/")
    fun deleteProject(@Path("id") id: Int): Call<Void>
}