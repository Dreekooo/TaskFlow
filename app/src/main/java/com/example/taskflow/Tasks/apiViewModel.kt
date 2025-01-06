package com.example.taskflow.Tasks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.addProject.Project
import com.example.taskflow.addProject.ProjectInterface
import com.example.taskflow.addProject.users.User
import com.example.taskflow.addProject.users.UserInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiTaskViewModel : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val retrofit: Retrofit = Retrofit.Builder().baseUrl("http://192.168.68.114:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val api: TasksInterface = retrofit.create(TasksInterface::class.java)
    private val userApi: UserInterface = retrofit.create(UserInterface::class.java)


    init {
        fetchProjects()
        startAutoRefresh()
    }

    private fun fetchProjects() {
        fetchData({ api.getTasks() }, { projects ->
            _tasks.value = projects
        })
    }

    private fun startAutoRefresh() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                try {
                    fetchProjects()
                    delay(1000)
                } catch (e: Exception) {
                    Log.e("AUTO_REFRESH_ERROR", "Error during auto-refresh", e)
                }
            }
        }
    }

    private inline fun <T> fetchData(
        crossinline apiCall: suspend () -> T, crossinline onSuccess: (T) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = apiCall()
                Log.d("API_FETCH", "Fetched data: $result")
                onSuccess(result)
            } catch (e: Exception) {
                Log.e("API_FETCH_ERROR", "Error fetching data", e)
            }
        }
    }
}