package com.example.taskflow.User

import com.google.gson.annotations.SerializedName

data class UserRegister(
    @SerializedName("email") val email: String,
    @SerializedName("username") val username: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("password_hash") val password: String,
)

data class UserLogin(
    @SerializedName("identifier") val username: String,
    @SerializedName("password") val password: String,
)

data class LoginResponse(
    @SerializedName("message") val message: String,
    @SerializedName("token") val token: String
)
