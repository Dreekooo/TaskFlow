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

    private val retrofit: Retrofit = Retrofit.Builder().baseUrl("http://192.168.68.106:8080/")
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

    private val _selectedRoles = MutableStateFlow<List<Int>>(emptyList())
    val selectedRoles: StateFlow<List<Int>> = _selectedRoles

    fun onRoleSelectionChanged(roleId: Int) {
        _selectedRoles.value = if (_selectedRoles.value.contains(roleId)) {
            _selectedRoles.value - roleId
        } else {
            _selectedRoles.value + roleId
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
            } else {
                rolesForUser.add(roleId)
                Log.d("USER_ROLE", "User $userId: Role $roleId added")
            }
            this[userId] = rolesForUser
        }
    }



}
