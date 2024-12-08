package com.example.taskflow.addProject

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.addProject.users.User
import com.example.taskflow.addProject.users.UserInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProjectsAPIViewModel : ViewModel() {
    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://99999999:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api: ProjectInterface = retrofit.create(ProjectInterface::class.java)
    private val userApi: UserInterface = retrofit.create(UserInterface::class.java)

    init {
        fetchProjects()
        startAutoRefresh()
    }

    private fun fetchProjects() {
        fetchData({ api.getProjects() }, { projects ->
            _projects.value = projects
        })
    }

    fun fetchUserById(userId: Int, onSuccess: (User) -> Unit) {
        fetchData({ userApi.getUser(userId) }, onSuccess)
    }

    private fun startAutoRefresh() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                try {
                    fetchProjects()
                    delay(5000)
                } catch (e: Exception) {
                    Log.e("AUTO_REFRESH_ERROR", "Error during auto-refresh", e)
                }
            }
        }
    }

    private inline fun <T> fetchData(
        crossinline apiCall: suspend () -> T,
        crossinline onSuccess: (T) -> Unit
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
