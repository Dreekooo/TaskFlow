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

    @DELETE("tasks/{id}/")
    fun deleteTask(@Path("id") taskId: Int): Call<Void>

    @PUT("tasks/{id}/")
    fun updateTask(
        @Path("id") task_id: Int,
        @Body updatedTask: PostTask
    ): Call<Void>


}