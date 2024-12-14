package com.example.taskflow.addProject.projectUsers

import android.telecom.Call
import com.example.taskflow.addProject.Project
import retrofit2.http.Body
import retrofit2.http.POST

interface ProjectUsersInterface {

    @POST("projects")
    fun addProject(@Body project: Project): Call

    @POST("project-users")
    fun addProjectUser(@Body projectUser: ProjectUser): Call
}