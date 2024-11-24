package com.example.taskflow.addProject

import retrofit2.http.GET

interface ProjectInterface {
    @GET("projects/")
    suspend fun getProjects(): List<Project>
}