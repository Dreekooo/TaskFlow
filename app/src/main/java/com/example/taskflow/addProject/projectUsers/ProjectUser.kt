package com.example.taskflow.addProject.projectUsers

import com.google.gson.annotations.SerializedName

data class ProjectUser(
    @SerializedName("project_user_id") val id: Int,
    @SerializedName("project_id") val projectId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("role") val role: Int
)