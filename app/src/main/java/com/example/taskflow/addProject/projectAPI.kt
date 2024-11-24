package com.example.taskflow.addProject

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ProjectList(viewModel: projectsViewModel, modifier: Modifier = Modifier) {
    val projects by viewModel.projects.collectAsState()

    Log.d("APICOS", "${projects.size}")

    LazyColumn(modifier = modifier) {
        if (projects.isEmpty()) {
            item { Text(text = "No projects available") }
        } else {
            items(projects) { project ->
                Text(text = "Project ID: ${project.id}, Name: ${project.name}, desc")
                Log.d("API", "Project ID: ${project.id}, Name: ${project.name}")
            }
        }
    }
}