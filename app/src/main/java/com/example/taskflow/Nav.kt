package com.example.taskflow

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskflow.addProject.ProjectList
import com.example.taskflow.addProject.ProjectViewModel
import com.example.taskflow.addProject.ProjectsAPIViewModel

@Composable
fun Nav(

) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            ProjectList(
                apiViewModel = ProjectsAPIViewModel(),
                projectViewModel = ProjectViewModel()
            )
        }
        composable("project") {

        }
    }
}