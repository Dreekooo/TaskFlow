package com.example.taskflow.Tasks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date

class taskViewModel : ViewModel() {

    var selectedTaskType by mutableStateOf<Int?>(null)
    var isDialogShow by mutableStateOf(false)
    var taskTitle by mutableStateOf("")
    var descriptionTask by mutableStateOf("")


    fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        return dateFormat.format(date)
    }

    fun showDialog() {
        isDialogShow = true
    }

    fun onDismissRequest() {
        isDialogShow = false
    }
}