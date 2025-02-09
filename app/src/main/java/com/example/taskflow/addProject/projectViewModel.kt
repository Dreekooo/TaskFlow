package com.example.taskflow.addProject

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.sql.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class ProjectViewModel : ViewModel() {
    var projectViewModelError by mutableStateOf("")
    var selectedDate by mutableStateOf("")
    var enabled by mutableStateOf(false)
    var projectDescription by mutableStateOf("")
    var projectName by mutableStateOf("")
    var roleName by mutableStateOf("")
    var isDialogShow by mutableStateOf(false)
    var isEdit by mutableStateOf(false)
    var expandedId by mutableStateOf<Int?>(null)
    var deadline by mutableStateOf("")
    val deadlineTimestamp = parseDateToTimestamp(deadline)

    fun onDismissRequest() {
        isDialogShow = false
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun formatTimestamp(timestamp: Timestamp): String {
        val localDate = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        return localDate.format(formatter)
    }

    fun parseDateToTimestamp(date: String): Timestamp? {
        if (date.isBlank()) {
            return null
        }

        return try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val parsedDate = dateFormat.parse(date)
            Timestamp(parsedDate.time)
        } catch (e: ParseException) {
            null
        }
    }


    fun editProject() {
        isDialogShow = true
        isEdit = true
    }

}