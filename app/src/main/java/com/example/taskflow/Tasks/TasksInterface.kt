package com.example.taskflow.Tasks

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TasksInterface {
    @GET("tasks/")
    suspend fun getTasks(): List<Task>

    @POST("tasks/")
    suspend fun addTasks(@Body task: PostTask): Response<Void>

    @DELETE("tasks/{task_id}/")
    fun deleteTask(@Path("task_id") taskId: Int): Call<Void>

    @PUT("tasks/{task_id}")
    fun updateTask(
        @Path("task_id") task_id: Int,
        @Body updatedTask: PostTask
    ): Call<Void>


}