package com.example.taskflow.addProject.users

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class User(
    @SerializedName("user_id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("username") val username: String,
    @SerializedName("first_name") val first_name: String,
    @SerializedName("last_name") val last_name: String,
    @SerializedName("created_by") val created_at: Timestamp
)


