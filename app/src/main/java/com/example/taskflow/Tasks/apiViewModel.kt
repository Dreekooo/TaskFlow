package com.example.taskflow.Tasks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.addProject.ProjectViewModel
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
import java.util.Date

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

    private val _selectedUsers = MutableStateFlow<List<Int>>(emptyList())
    val selectedUsers: StateFlow<List<Int>> = _selectedUsers

    fun onSelectionChanged(userId: Int) {
        _selectedUsers.value = if (_selectedUsers.value.contains(userId)) {
            _selectedUsers.value - userId
        } else {
            _selectedUsers.value + userId
        }
    }


    fun addTaskToProject(task: PostTask) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.addTasks(task)
                if (response.isSuccessful) {
                    Log.d("success", "Task added successfully")
                } else {
                    Log.e("error", "Error from API: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("exception", "Exception occurred: ${e.message}")
            }
        }
    }

    fun addTask(
        taskTitle: String,
        description: String,
        type: Int,
        date: Date,
        projectViewModel: ProjectViewModel
    ) {
        val task =
            projectViewModel.expandedId?.let {
                PostTask(
                    title = taskTitle,
                    description = description,
                    priority = type,
                    due_date = date,
                    projectID = it
                )
            }
        if (task != null) {
            addTaskToProject(task)
        }
    }

    fun deleteTaskById(taskId: Int) {
        val apiService: TasksInterface = retrofit.create(TasksInterface::class.java)
        val call = apiService.deleteTask(taskId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.e("IDDDD", taskId.toString())
                if (response.isSuccessful) {
                    println("Zadanie o ID $taskId zostało pomyślnie usunięte.")
                } else {
                    println("Nie udało się usunąć zadania. Kod odpowiedzi: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Wystąpił błąd podczas próby usunięcia zadania: ${t.message}")
            }
        })

    }


    fun updateTaskById(task_id: Int, updatedTask: PostTask) {
        val apiService: TasksInterface = retrofit.create(TasksInterface::class.java)
        val call = apiService.updateTask(task_id, updatedTask)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    println("Zadanie o task_id $task_id zostało pomyślnie zaktualizowane.")
                } else {
                    println("Nie udało się zaktualizować zadania. Kod odpowiedzi: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Wystąpił błąd podczas próby aktualizacji zadania: ${t.message}")
            }
        })
    }

}