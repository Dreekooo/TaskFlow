package com.example.taskflow.buttons

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.PersonAddAlt1
import androidx.compose.material.icons.rounded.Task
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.example.taskflow.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.taskflow.Tasks.ApiTaskViewModel
import com.example.taskflow.Tasks.PostTask
import com.example.taskflow.Tasks.taskViewModel
import com.example.taskflow.addProject.ProjectPost
import com.example.taskflow.addProject.ProjectViewModel
import com.example.taskflow.addProject.ProjectsAPIViewModel
import java.util.regex.Pattern

@Composable
fun CloseButton(
    viewModel: ProjectViewModel,
) {
    FloatingActionButton(
        onClick = {
            viewModel.isDialogShow = false
        },
        modifier = Modifier.size(70.dp),
        shape = CircleShape,
        contentColor = colorResource(R.color.button_description),
        containerColor = colorResource(R.color.button_background)
    ) {
        Icon(
            Icons.Rounded.Close,
            contentDescription = stringResource(R.string.plus_content),
            modifier = Modifier.size(34.dp)
        )
    }
}

@Composable
fun CloseButton(
    taskViewModel: taskViewModel,
) {
    FloatingActionButton(
        onClick = {
            taskViewModel.isDialogShow = false
        },
        modifier = Modifier.size(70.dp),
        shape = CircleShape,
        contentColor = colorResource(R.color.button_description),
        containerColor = colorResource(R.color.button_background)
    ) {
        Icon(
            Icons.Rounded.Close,
            contentDescription = stringResource(R.string.plus_content),
            modifier = Modifier.size(34.dp)
        )
    }
}

@Composable
fun AddTasksButton(
    taskViewModel: taskViewModel
) {
    FloatingActionButton(
        onClick = {
            taskViewModel.isDialogShow = true
        },
        modifier = Modifier.size(70.dp),
        shape = CircleShape,
        contentColor = colorResource(R.color.button_description),
        containerColor = colorResource(R.color.button_background)
    ) {
        Icon(
            Icons.Rounded.Add,
            contentDescription = stringResource(R.string.plus_content),
            modifier = Modifier.size(34.dp)
        )
    }
}

@Composable
fun SubmitButton(
    projectViewModel: ProjectViewModel,
    apiViewModel: ProjectsAPIViewModel,
) {
    FloatingActionButton(
        onClick = {
            editOrAdd(apiViewModel, projectViewModel)
        },
        modifier = Modifier.size(70.dp),
        shape = CircleShape,
        contentColor = colorResource(R.color.button_description),
        containerColor = colorResource(R.color.button_background)
    ) {
        Icon(
            Icons.Rounded.Check, contentDescription = "", modifier = Modifier.size(34.dp)
        )
    }
}


@Composable
fun SubmitButtonTask(
    taskViewModel: taskViewModel,
    apiTaskViewModel: ApiTaskViewModel
) {
    FloatingActionButton(
        onClick = {
            val datePattern =
                "^(0[1-9]|1[0-9]|2[0-9]|3[01])\\.(0[1-9]|1[0-2])\\.(\\d{4})$"
            val regex = Pattern.compile(datePattern)


            fun isValidDate(date: String): Boolean {
                return regex.matcher(date).matches()
            }


            val result = isValidDate("12.01.2025")
            println("Result: $result")

            taskViewModel.Valid = isValidDate(taskViewModel.date1)

            Log.d("Data123: ", taskViewModel.date1)
            Log.d("Data123: ", taskViewModel.Valid.toString())


            if (!taskViewModel.Valid) {
                taskViewModel.error = "błąd 1 data"
            }

            if (!taskViewModel.Valid2) {
                taskViewModel.error1 = "błąd 2 data"
            }
            if (taskViewModel.Valid) {
                if (taskViewModel.update) {
                    val taskEdit = taskViewModel.selectedTaskType?.let {
                        taskViewModel.stringToDate(taskViewModel.date1)?.let { it1 ->
                            PostTask(
                                title = taskViewModel.taskTitle,
                                description = taskViewModel.descriptionTask,
                                priority = it,
                                end = it1
                            )
                        }
                    }
                    taskViewModel.expandedId?.let {
                        if (taskEdit != null) {
                            apiTaskViewModel.updateTaskById(it, taskEdit)
                        }
                    }
                } else {
                    taskViewModel.selectedTaskType?.let {
                        taskViewModel.stringToDate(taskViewModel.date1)?.let { it1 ->
                            apiTaskViewModel.addTask(
                                taskViewModel.taskTitle,
                                taskViewModel.descriptionTask,
                                it,
                                it1
                            )
                        }
                    }
                    taskViewModel.onDismissRequest()
                }
            }
        },
        modifier = Modifier.size(70.dp),
        shape = CircleShape,
        contentColor = colorResource(R.color.button_description),
        containerColor = colorResource(R.color.button_background)
    ) {
        Icon(
            Icons.Rounded.Check, contentDescription = "", modifier = Modifier.size(34.dp)
        )
    }
}


@Composable
fun AddProjectButton(
    projectViewModel: ProjectViewModel,
) {
    FloatingActionButton(
        onClick = {
            projectViewModel.isDialogShow = true
        },
        modifier = Modifier.size(70.dp),
        shape = CircleShape,
        contentColor = colorResource(R.color.button_description),
        containerColor = colorResource(R.color.button_background)
    ) {
        Icon(
            Icons.Rounded.Add,
            contentDescription = stringResource(R.string.plus_content),
            modifier = Modifier.size(34.dp)
        )
    }
}


fun fetchProject(apiViewModel: ProjectsAPIViewModel, projectViewModel: ProjectViewModel) {
    projectViewModel.parseDateToTimestamp(projectViewModel.deadline)?.let {
        apiViewModel.addProjectRoles(
            projectViewModel.projectName,
            projectViewModel.projectDescription,
            1,
            it,
            apiViewModel.userRoles.value,
        )
    }
}

fun editOrAdd(apiViewModel: ProjectsAPIViewModel, projectViewModel: ProjectViewModel) {
    val deadlineTimestamp = projectViewModel.parseDateToTimestamp(projectViewModel.deadline)

    if (deadlineTimestamp == null) {
        projectViewModel.projectViewModelError = "Nieprawidłowy format daty"
        return
    }

    val projectPost = ProjectPost(
        name = projectViewModel.projectName,
        description = projectViewModel.projectDescription,
        created_by = 1,
        deadline = deadlineTimestamp
    )

    if (projectViewModel.isEdit) {
        projectViewModel.expandedId?.let { projectId ->
            apiViewModel.updateProjectById(projectId, projectPost)

            apiViewModel.deleteProjectUsersByProjectId(projectId)

            val userRoles = apiViewModel.userRoles.value
            userRoles.forEach { (userId, roleIds) ->
                roleIds.forEach { roleId ->
                    apiViewModel.addProjectUser(projectId, userId, roleId)
                }
            }
        }
    } else {
        fetchProject(apiViewModel, projectViewModel)
    }
    onDismissRequest(projectViewModel, apiViewModel)
}


fun onDismissRequest(projectViewModel: ProjectViewModel, apiViewModel: ProjectsAPIViewModel) {
    projectViewModel.isDialogShow = false
    projectViewModel.enabled = false
    projectViewModel.projectDescription = ""
    projectViewModel.projectName = ""
    projectViewModel.roleName = ""
    projectViewModel.isEdit = false
    apiViewModel.clearUserRoles()
}


@Composable
fun AddRoleButton(
    apiViewModel: ProjectsAPIViewModel, viewModel: ProjectViewModel
) {
    FloatingActionButton(
        onClick = {
            apiViewModel.addRole(viewModel.roleName)
            viewModel.roleName = ""
        },
        modifier = Modifier.size(50.dp),
        shape = RoundedCornerShape(10.dp),
        contentColor = colorResource(R.color.button_description),
        containerColor = colorResource(R.color.button_background)
    ) {
        Icon(
            Icons.Rounded.PersonAddAlt1,
            contentDescription = stringResource(R.string.plus_content),
            modifier = Modifier.size(34.dp)
        )
    }
}

@Composable
fun DeleteButton(
    apiViewModel: ProjectsAPIViewModel, projectID: Int
) {
    FloatingActionButton(
        onClick = {
            apiViewModel.deleteProjectById(projectID)
        },
        modifier = Modifier.size(70.dp),
        shape = CircleShape,
        contentColor = colorResource(R.color.button_description),
        containerColor = colorResource(R.color.button_background)
    ) {
        Icon(
            Icons.Rounded.Delete,
            contentDescription = stringResource(R.string.plus_content),
            modifier = Modifier.size(34.dp)
        )
    }
}

@Composable
fun EditButton(
    projectViewModel: ProjectViewModel,
    apiViewModel: ProjectsAPIViewModel
) {
    FloatingActionButton(
        onClick = {
            projectViewModel.editProject()
            dataToEdit(projectViewModel, apiViewModel)
        },
        modifier = Modifier.size(70.dp),
        shape = CircleShape,
        contentColor = colorResource(R.color.button_description),
        containerColor = colorResource(R.color.button_background)
    ) {
        Icon(
            Icons.Rounded.Edit,
            contentDescription = stringResource(R.string.plus_content),
            modifier = Modifier.size(34.dp)
        )
    }
}

@Composable
fun EditTaskButton(
    taskViewModel: taskViewModel
) {
    FloatingActionButton(
        onClick = {
            taskViewModel.isDialogShow = true
            taskViewModel.update = true
        },
        modifier = Modifier.size(70.dp),
        shape = CircleShape,
        contentColor = colorResource(R.color.button_description),
        containerColor = colorResource(R.color.button_background)
    ) {
        Icon(
            Icons.Rounded.Edit,
            contentDescription = stringResource(R.string.plus_content),
            modifier = Modifier.size(34.dp)
        )
    }
}


@Composable
fun DeleteTaskButton(
    taskId: Int,
    apiTaskViewModel: ApiTaskViewModel
) {
    FloatingActionButton(
        onClick = {
            apiTaskViewModel.deleteTaskById(taskId)
        },
        modifier = Modifier.size(70.dp),
        shape = CircleShape,
        contentColor = colorResource(R.color.button_description),
        containerColor = colorResource(R.color.button_background)
    ) {
        Icon(
            Icons.Rounded.Delete,
            contentDescription = stringResource(R.string.plus_content),
            modifier = Modifier.size(34.dp)
        )
    }
}

@Composable
fun ProjectsTaskBtn() {
    FloatingActionButton(
        onClick = {

        },
        modifier = Modifier.size(70.dp),
        shape = CircleShape,
        contentColor = colorResource(R.color.button_description),
        containerColor = colorResource(R.color.button_background)
    ) {
        Icon(
            Icons.Rounded.Task,
            contentDescription = stringResource(R.string.plus_content),
            modifier = Modifier.size(34.dp)
        )
    }
}

fun dataToEdit(
    projectViewModel: ProjectViewModel, apiViewModel: ProjectsAPIViewModel
) {

    projectViewModel.expandedId?.let { apiViewModel.fetchProjectById(it) }

    val project = apiViewModel.selectedProject



    projectViewModel.projectName = project.value?.name ?: ""
    projectViewModel.projectDescription = project.value?.description ?: ""


}