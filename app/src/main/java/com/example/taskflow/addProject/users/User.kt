package com.example.taskflow.addProject.users

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class User(
    @SerializedName("user_id") val id: Int,
    val email: String,
    val username: String,
    val first_name: String,
    val last_name: String,
    val created_at: Timestamp
)
