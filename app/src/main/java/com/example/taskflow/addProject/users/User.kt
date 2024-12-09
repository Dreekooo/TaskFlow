package com.example.taskflow.addProject.users

import java.sql.Timestamp

data class User(
    val id: Int,
    val email: String,
    val username: String,
    val first_name: String,
    val last_name: String,
    val created_at: Timestamp
)
