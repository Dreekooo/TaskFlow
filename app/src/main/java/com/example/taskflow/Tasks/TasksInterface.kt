package com.example.taskflow.Tasks

import com.example.taskflow.addProject.Project
import retrofit2.http.GET

interface TasksInterface {
    @GET("tasks/")
    suspend fun getTasks(): List<Task>
}