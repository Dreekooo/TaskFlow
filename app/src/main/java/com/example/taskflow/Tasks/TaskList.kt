package com.example.taskflow.Tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.taskflow.buttons.AddTasksButton

@Composable
fun TasksList(
    apiTaskViewModel: ApiTaskViewModel,
    taskViewModel: taskViewModel,
    projectViewModel: ProjectViewModel,
    apiViewModel: ProjectsAPIViewModel,
    onNavigateBack: () -> Unit
) {
    val tasks by apiTaskViewModel.tasks.collectAsState()
    val expandedID = projectViewModel.expandedId

    val filteredTasks = tasks.filter { task ->
        task.projectID == expandedID
    }



    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "",
                    )

                }
            }
        },
        bottomBar = {
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

            if (filteredTasks.isEmpty()) {
                item { Text(text = "No projects available") }
            } else {
                items(filteredTasks) { task ->

                    TaskView(
                        taskID = task.id,
                        taskName = task.title,
                        taskType = task.priority,
                        description = task.description,
                        taskCreated = task.created,
                        taskEnd = task.end,
                        isExpanded = taskViewModel.expandedId == task.id,
                        onClick = {
                            taskViewModel.expandedId =
                                if (taskViewModel.expandedId == task.id) null else task.id
                        },
                        taskViewModel = taskViewModel,
                        apiTaskViewModel = apiTaskViewModel
                    )
                }
            }
        }

        if (taskViewModel.isDialogShow) {
            addTaskDialog(
                taskViewModel = taskViewModel,
                apiViewModel = apiViewModel,
                projectViewModel
            )
        }
    }
}