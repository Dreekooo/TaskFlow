package com.example.taskflow

import LoginForm
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.taskflow.User.ApiViewModel
import com.example.taskflow.User.ViewModel
import com.example.taskflow.addProject.ProjectViewModel
import com.example.taskflow.addProject.ProjectsAPIViewModel
import com.example.taskflow.ui.theme.TaskFLowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskFLowTheme {
                // LoginForm(ViewModel(), ApiViewModel())


                Nav(
                    apiViewModel = ProjectsAPIViewModel(),
                    projectViewModel = ProjectViewModel(),
                    ViewModel(), ApiViewModel()
                )
            }
        }
    }
}


