package com.example.taskflow.addProject

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.addProject.projectUsers.POSTUsersInterface
import com.example.taskflow.addProject.projectUsers.ProjectUser
import com.example.taskflow.addProject.projectUsers.ProjectUserPOST
import com.example.taskflow.addProject.users.User
import com.example.taskflow.addProject.users.UserInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProjectsAPIViewModel : ViewModel() {
    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _allUsers =
        MutableStateFlow<List<User>>(emptyList())
    val allUsers: StateFlow<List<User>> = _allUsers

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.68.106:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api: ProjectInterface = retrofit.create(ProjectInterface::class.java)
    private val userApi: UserInterface = retrofit.create(UserInterface::class.java)


    init {
        fetchProjects()
        fetchAllUsers(1)
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

    private fun fetchAllUsers(excludedId: Int) {
        fetchData({ userApi.getAllUsers() }, { users ->
            val filteredUsers = users.filter { it.id != excludedId }
            _allUsers.value = filteredUsers
            Log.d("FILTERED_USERS", "Users excluding ID $excludedId: $filteredUsers")
        })
    }

    private fun startAutoRefresh() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                try {
                    fetchProjects()
                    fetchAllUsers(1)
                    delay(1000)
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

    private val _selectedUsers = MutableStateFlow<List<Int>>(emptyList())
    val selectedUsers: StateFlow<List<Int>> = _selectedUsers

    fun onSelectionChanged(userId: Int) {
        _selectedUsers.value = if (_selectedUsers.value.contains(userId)) {
            _selectedUsers.value - userId
        } else {
            if (_selectedUsers.value.size < 2) {
                _selectedUsers.value + userId
            } else {
                _selectedUsers.value.drop(1) + userId
            }
        }
    }

    fun addProjectUser(projectId: Int, userId: Int, role: Int) {
        val projectUser = ProjectUserPOST(projectId, userId, role)

        val projectUserApi = retrofit.create(POSTUsersInterface::class.java)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = projectUserApi.addProjectUser(projectUser)
            } catch (e: Exception) {
                Log.e("API_EXCEPTION", "Exception: $e")
            }
        }
    }


    fun addProject(name: String, description: String, createdBy: Int) {
        val project = ProjectPost(name, description, createdBy)

        Log.d("dupa", "dupa")
        val projectApi = retrofit.create(POSTUsersInterface::class.java)

        projectApi.addProject(project).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("API_SUCCESS", "Project added successfully!")
                } else {
                    Log.e("API_ERROR", "Error adding project: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API_EXCEPTION", "Exception: $t")
            }
        })
    }


}
