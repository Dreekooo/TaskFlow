package com.example.taskflow.Tasks

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Task(
    @SerializedName("task_id") val id: Int,
    @SerializedName("project_id") val projectID: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("start_date") val created: Date,
    @SerializedName("due_date") val end: Date,
    @SerializedName("priority") val priority: Int,
)

data class PostTask(
    @SerializedName("title") val title: String,
    @SerializedName("priority") val priority: Int,
    @SerializedName("description") val description: String,
    @SerializedName("due_date") val due_date: Date,
    @SerializedName("project_id") val projectID: Int,
)
