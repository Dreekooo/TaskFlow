package com.example.taskflow.addProject.projectUsers

import retrofit2.Call
import com.example.taskflow.addProject.ProjectPost
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface POSTUsersInterface {

    @POST("projects/")
    fun addProject(@Body project: ProjectPost): Call<Response<Void>>

    @POST("project-users/")
    fun addProjectUser(@Body projectUser: ProjectUserPOST): Call<Void>
}