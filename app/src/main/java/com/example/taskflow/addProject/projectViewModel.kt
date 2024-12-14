package com.example.taskflow.addProject

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ProjectViewModel : ViewModel() {

    var enabled by mutableStateOf(false)
    var projectDescription by mutableStateOf("")
    var projectName by mutableStateOf("")
    var roleName by mutableStateOf("")
    var isDialogShow by mutableStateOf(false)


    fun onDismissRequest() {
        isDialogShow = false
    }
}