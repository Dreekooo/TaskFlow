package com.example.taskflow.User

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ViewModel : ViewModel() {
    var loggedUser by mutableStateOf("")
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var email by mutableStateOf("")
    var resultMessage by mutableStateOf("")
    var userId by mutableStateOf<String?>(null)
    var token by mutableStateOf<String?>(null)
    var isRegister by mutableStateOf(false)
    var logged by mutableStateOf(false)

    fun onDismiss() {
        username = ""
        password = ""
        firstName = ""
        lastName = ""
        email = ""
    }
}