package com.example.taskflow.addProject.projectUsers

import retrofit2.Call
import com.example.taskflow.addProject.ProjectPost
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface POSTUsersInterface {

    @POST("projects/")
    fun addProject(@Body project: ProjectPost): Call<Response<Void>>

    @POST("project-users/")
    fun addProjectUser(@Body projectUser: ProjectUserPOST): Call<Void>

    @PUT("project-users/{id}")
    fun updateProject(@Body project: ProjectPost): Call<Response<Void>>

    @GET("project-users/")
    fun getAllProjectUsers(): ProjectUserPOST

    @PUT("project-users/")
    fun updateProjectUser(@Body projectUser: ProjectUserPOST): Call<Void>
}