package com.example.taskflow.Tasks

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskflow.addProject.ProjectViewModel
import com.example.taskflow.addProject.ProjectsAPIViewModel
import com.example.taskflow.buttons.AddProjectButton
import com.example.taskflow.buttons.AddTasksButton

@Composable
fun TasksList(
    apiTaskViewModel: ApiTaskViewModel,
    taskViewModel: taskViewModel,
    apiViewModel: ProjectsAPIViewModel
) {
    val tasks by apiTaskViewModel.tasks.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                AddTasksButton(
                    taskViewModel,
                )
            }

        }
    }) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (tasks.isEmpty()) {
                item { Text(text = "No projects available") }
            } else {
                items(tasks) { task ->

                    TaskView(
                        taskID = task.id,
                        taskName = task.title,
                        taskType = task.priority,
                        taskCreated = task.created,
                        taskEnd = task.end,
                        isExpanded = false,
                        onClick = {

                        },
                        apiViewModel = apiTaskViewModel,
                        taskViewModel = taskViewModel()
                    )
                }
            }
        }

        if (taskViewModel.isDialogShow) {
            addTaskDialog(
                taskViewModel = taskViewModel,
                apiViewModel = apiViewModel
            )
        }
        Log.d("shoDialog", taskViewModel.isDialogShow.toString())

    }
}