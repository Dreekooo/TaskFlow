package com.example.taskflow.User

import com.google.gson.annotations.SerializedName

data class UserRegister(
    @SerializedName("email") val email: String,
    @SerializedName("username") val username: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("password_hash") val password: String,
)
