package com.example.taskflow.addProject

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class projectsViewModel : ViewModel() {
    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects

    private val api: ProjectInterface = Retrofit.Builder()
        .baseUrl("http://9999999999:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ProjectInterface::class.java)

    init {
        fetchProjects()
    }

    private fun fetchProjects() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fetchedProjects = api.getProjects()
                Log.d("API_FETCH", "Fetched Projects: $fetchedProjects")
                _projects.value = fetchedProjects
            } catch (e: Exception) {
                Log.e("API_FETCH_ERROR", "Error fetching projects", e)
            }
        }
    }
}