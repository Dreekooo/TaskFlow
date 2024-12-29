package com.example.taskflow.addProject.projectUsers

import com.google.gson.annotations.SerializedName

data class ProjectUser(
    @SerializedName("project_user_id") val id: Int,
    @SerializedName("project_id") val projectId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("role") val role: Int
)


data class ProjectUserPOST(
    @SerializedName("project_id") val projectId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("role") val role: Int
)

data class ProjectResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("created_by") val createdBy: Int
)