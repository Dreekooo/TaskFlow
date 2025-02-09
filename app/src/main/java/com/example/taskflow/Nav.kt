package com.example.taskflow

import LoginForm
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskflow.Tasks.ApiTaskViewModel
import com.example.taskflow.Tasks.TasksList
import com.example.taskflow.Tasks.taskViewModel
import com.example.taskflow.User.ApiViewModel
import com.example.taskflow.User.ViewModel
import com.example.taskflow.addProject.ProjectList
import com.example.taskflow.addProject.ProjectViewModel
import com.example.taskflow.addProject.ProjectsAPIViewModel

@Composable
fun Nav(
    apiViewModel: ProjectsAPIViewModel,
    projectViewModel: ProjectViewModel,
    viewModel: ViewModel,
    userApi: ApiViewModel,

    ) {
    val navController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginForm(
                viewModel, userApi,
                onNavigate = {
                    navController.navigate("home")
                },
                userApi = userApi,
                projectsAPIViewModel = apiViewModel,
            )
        }

        composable("home") {
            ProjectList(
                apiViewModel = apiViewModel,
                projectViewModel = projectViewModel,
                onNavigateToTask = {
                    navController.navigate("task")
                },
                apiUser = userApi
            )
        }
        composable("task") {
            TasksList(
                apiViewModel = apiViewModel,
                taskViewModel = taskViewModel(),
                apiTaskViewModel = ApiTaskViewModel(),
                onNavigateBack = {
                    navController.popBackStack()
                },
                projectViewModel = projectViewModel
            )
        }
    }
}