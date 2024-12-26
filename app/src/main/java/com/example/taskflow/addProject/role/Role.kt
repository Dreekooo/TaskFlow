package com.example.taskflow.addProject.role

import com.google.gson.annotations.SerializedName

data class Role(
    @SerializedName("role_id") val id: Int,
    val role_name: String
)

data class RolePost(
    val role_name: String
)