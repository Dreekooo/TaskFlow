package com.example.taskflow.Tasks

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Task(
    @SerializedName("task_id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("start_date") val created: Date,
    @SerializedName("due_date") val end: Date,
    @SerializedName("priority") val priority: Int,
)
