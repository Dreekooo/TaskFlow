package com.example.taskflow.Tasks

import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date

class taskViewModel : ViewModel() {

    fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        return dateFormat.format(date)

    }
}