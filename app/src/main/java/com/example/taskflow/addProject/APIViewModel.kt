package com.example.taskflow.addProject

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.addProject.projectUsers.POSTUsersInterface
import com.example.taskflow.addProject.projectUsers.ProjectUserPOST
import com.example.taskflow.addProject.role.Role
import com.example.taskflow.addProject.role.RoleInterface
import com.example.taskflow.addProject.role.RolePost
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

    private val _allUsers = MutableStateFlow<List<User>>(emptyList())
    val allUsers: StateFlow<List<User>> = _allUsers

    private val retrofit: Retrofit = Retrofit.Builder().baseUrl("http://192.168.68.114:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val api: ProjectInterface = retrofit.create(ProjectInterface::class.java)
    private val userApi: UserInterface = retrofit.create(UserInterface::class.java)


    init {
        fetchProjects()
        fetchAllUsers(1)
        startAutoRefresh()
        getAllRole()
    }

    private fun fetchProjects() {
        fetchData({ api.getProjects() }, { projects ->
            _projects.value = projects
        })
    }


    private val _selectedProject = MutableStateFlow<Project?>(null)
    val selectedProject: StateFlow<Project?> = _selectedProject
    fun fetchProjectById(projectId: Int) {
        fetchData(
            { api.getProject(projectId) },
            { project ->
                _selectedProject.value = project
            }
        )
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
                    getAllRole()
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


    fun addProjectUser(projectId: Int, userId: Int, role: Int) {
        val projectUser = ProjectUserPOST(projectId, userId, role)

        val projectUserApi = retrofit.create(POSTUsersInterface::class.java)

        projectUserApi.addProjectUser(projectUser).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d(
                        "API_SUCCESS",
                        "User $userId assigned to project $projectId with role $role"
                    )
                } else {
                    Log.e("API_ERROR", "Error assigning user to project: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API_EXCEPTION", "Failed to assign user to project: $t")
            }
        })
    }


    fun addProjectRoles(
        projectName: String,
        projectDescription: String,
        createdBy: Int,
        userRoles: Map<Int, Set<Int>>
    ) {
        val project = ProjectPost(projectName, projectDescription, createdBy)

        val projectApi = retrofit.create(POSTUsersInterface::class.java)

        projectApi.addProject(project).enqueue(object : Callback<Response<Void>> {
            override fun onResponse(
                call: Call<Response<Void>>,
                response: Response<Response<Void>>
            ) {
                if (response.isSuccessful) {

                    val projectWithMaxId: Project? = projects.value.maxByOrNull { it.id }

                    Log.d("users", userRoles.size.toString())
                    Log.d("API_SUCCESS", "Project created successfully!")

                    userRoles.forEach { (userId, roleIds) ->
                        roleIds.forEach { roleId ->
                            if (projectWithMaxId != null) {
                                addProjectUser(
                                    projectWithMaxId.id + 1,
                                    userId,
                                    roleId
                                )
                            } else {
                                addProjectUser(
                                    1,
                                    userId,
                                    roleId
                                )
                            }
                        }
                    }
                } else {
                    Log.e("API_ERROR", "Error adding project: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Response<Void>>, t: Throwable) {
                Log.e("API_EXCEPTION", "Failed to create project: $t")
            }
        })
    }


    fun addRole(name: String) {
        val role = RolePost(name)
        val roleApi = retrofit.create(RoleInterface::class.java)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = roleApi.addRole(role)
                response.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Log.d("API_SUCCESS", "Role added successfully!")
                        } else {
                            Log.e("API_ERROR", "Error adding role: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.e("API_EXCEPTION", "Exception: $t")
                    }
                })
            } catch (e: Exception) {
                Log.e("ADD_ROLE_ERROR", "Error adding role", e)
            }
        }
    }

    private val _roles = MutableStateFlow<List<Role>>(emptyList())
    val roles: StateFlow<List<Role>> = _roles

    fun getAllRole() {
        val roleApi = retrofit.create(RoleInterface::class.java)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val roles = roleApi.getRole()
                _roles.value = roles
                Log.d("API_SUCCESS", "Roles fetched successfully: $roles")
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error fetching roles", e)
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

    private val _userRoles = MutableStateFlow<Map<Int, Set<Int>>>(emptyMap())
    val userRoles: StateFlow<Map<Int, Set<Int>>> = _userRoles

    fun onUserRoleSelectionChanged(userId: Int, roleId: Int) {
        _userRoles.value = _userRoles.value.toMutableMap().apply {
            val rolesForUser = this[userId]?.toMutableSet() ?: mutableSetOf()

            if (rolesForUser.contains(roleId)) {
                rolesForUser.remove(roleId)
                Log.d("USER_ROLE", "User $userId: Role $roleId removed")
                if (rolesForUser.isEmpty()) {
                    this.remove(userId)
                } else {
                    this[userId] = rolesForUser
                }
            } else {
                rolesForUser.add(roleId)
                Log.d("USER_ROLE", "User $userId: Role $roleId added")
                this[userId] = rolesForUser
            }
        }
    }

    fun clearUserRoles() {
        _userRoles.value = emptyMap()

    }

    fun deleteProjectById(projectId: Int) {
        val apiService: ProjectInterface = retrofit.create(ProjectInterface::class.java)
        val call = apiService.deleteProject(projectId)

        call.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    println("Projekt o ID $projectId został pomyślnie usunięty.")
                } else {
                    println("Nie udało się usunąć projektu. Kod odpowiedzi: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Wystąpił błąd podczas próby usunięcia projektu: ${t.message}")
            }
        })
    }

    fun updateProjectById(projectId: Int, updatedProject: ProjectPost) {
        val apiService: ProjectInterface = retrofit.create(ProjectInterface::class.java)
        val call = apiService.updateProject(projectId, updatedProject)

        call.enqueue(object : Callback<Project> {
            override fun onResponse(call: Call<Project>, response: Response<Project>) {
                if (response.isSuccessful) {
                    val updatedProject = response.body()
                    println("Projekt został zaktualizowany: ${updatedProject?.name}")
                } else {
                    println("Błąd podczas aktualizacji projektu. Kod: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Project>, t: Throwable) {
                println("Nie udało się zaktualizować projektu: ${t.message}")
            }
        })
    }


}
