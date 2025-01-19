package com.example.taskflow

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskflow.Tasks.ApiTaskViewModel
import com.example.taskflow.Tasks.TasksList
import com.example.taskflow.Tasks.taskViewModel
import com.example.taskflow.addProject.ProjectList
import com.example.taskflow.addProject.ProjectViewModel
import com.example.taskflow.addProject.ProjectsAPIViewModel

@Composable
fun Nav(
    apiViewModel: ProjectsAPIViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            ProjectList(
                apiViewModel = apiViewModel,
                projectViewModel = ProjectViewModel(),
                onNavigateToTask = {
                    navController.navigate("task")
                }
            )
        }
        composable("task") {
            TasksList(
                apiViewModel = apiViewModel,
                taskViewModel = taskViewModel(),
                apiTaskViewModel = ApiTaskViewModel(),
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}