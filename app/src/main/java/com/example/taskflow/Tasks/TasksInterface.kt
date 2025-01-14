package com.example.taskflow.Tasks

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TasksInterface {
    @GET("tasks/")
    suspend fun getTasks(): List<Task>

    @POST("tasks/")
    suspend fun addTasks(@Body task: addTask): Response<Void>


}