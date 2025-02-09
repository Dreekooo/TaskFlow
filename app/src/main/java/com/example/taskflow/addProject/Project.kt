package com.example.taskflow.addProject

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class Project(
    @SerializedName("project_id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("created_by") val created_by: Int,
    @SerializedName("created_at") val deadline: Timestamp
)

data class ProjectPost(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("created_by") val created_by: Int
)


