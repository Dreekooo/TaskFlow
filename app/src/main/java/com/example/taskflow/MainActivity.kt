package com.example.taskflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.taskflow.Tasks.ApiTaskViewModel
import com.example.taskflow.Tasks.TasksList
import com.example.taskflow.Tasks.taskViewModel
import com.example.taskflow.addProject.ProjectList
import com.example.taskflow.addProject.ProjectViewModel
import com.example.taskflow.addProject.ProjectsAPIViewModel
import com.example.taskflow.ui.theme.TaskFLowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskFLowTheme {
//                ProjectList(ProjectsAPIViewModel(), projectViewModel = ProjectViewModel())

                TasksList(
                    apiTaskViewModel = ApiTaskViewModel(),
                    taskViewModel = taskViewModel(),
                    apiViewModel = ProjectsAPIViewModel()
                )
            }
        }
    }
}


