package com.example.taskflow.Tasks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class taskViewModel : ViewModel() {

    var selectedTaskType by mutableStateOf<Int?>(null)
    var isDialogShow by mutableStateOf(false)
    var taskTitle by mutableStateOf("")
    var descriptionTask by mutableStateOf("")
    var date1 by mutableStateOf("")
    var error by mutableStateOf("")
    var expandedId by mutableStateOf<Int?>(null)
    var update by mutableStateOf(false)
    fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        return dateFormat.format(date)
    }

    fun stringToDate(dateString: String): java.sql.Date? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        return try {
            val parsedDate = dateFormat.parse(dateString)
            java.sql.Date(parsedDate.time)
        } catch (e: Exception) {
            println("Błąd parsowania daty: ${e.message}")
            null
        }
    }

    fun onDismissRequest() {
        isDialogShow = false
        update = false
    }
}