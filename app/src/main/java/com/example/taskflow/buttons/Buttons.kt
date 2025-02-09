package com.example.taskflow.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.taskflow.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.taskflow.Tasks.ApiTaskViewModel
import com.example.taskflow.Tasks.PostTask
import com.example.taskflow.Tasks.taskViewModel
import com.example.taskflow.User.ApiViewModel
import com.example.taskflow.User.UserLogin
import com.example.taskflow.User.UserRegister
import com.example.taskflow.User.ViewModel
import com.example.taskflow.addProject.ProjectPost
import com.example.taskflow.addProject.ProjectViewModel
import com.example.taskflow.addProject.ProjectsAPIViewModel
import com.example.taskflow.ui.theme.backgroundDialog
import java.util.Calendar

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
    apiUser: ApiViewModel
) {
    FloatingActionButton(
        onClick = {
            editOrAdd(apiViewModel, projectViewModel,apiUser)
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
    taskViewModel: taskViewModel, apiTaskViewModel: ApiTaskViewModel,
    projectViewModel: ProjectViewModel
) {
    FloatingActionButton(
        onClick = {
            val calendar = Calendar.getInstance()
            val today = calendar.time

            if (taskViewModel.update) {
                val postTask = taskViewModel.selectedTaskType?.let {
                    projectViewModel.expandedId?.let { it1 ->
                        PostTask(
                            title = taskViewModel.taskTitle,
                            description = taskViewModel.descriptionTask,
                            priority = it,
                            due_date = today,
                            projectID = it1
                        )
                    }
                }
                if (postTask != null) {
                    taskViewModel.expandedId?.let {
                        apiTaskViewModel.updateTaskById(
                            task_id = it, updatedTask = postTask,
                        )
                    }
                }
            } else {
                taskViewModel.selectedTaskType?.let {
                    apiTaskViewModel.addTask(
                        taskViewModel.taskTitle, taskViewModel.descriptionTask, it, today,
                        projectViewModel
                    )
                }
            }
            taskViewModel.onDismissRequest()
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


fun fetchProject(
    apiViewModel: ProjectsAPIViewModel,
    projectViewModel: ProjectViewModel,
    apiUser: ApiViewModel
) {
    apiUser.currentUser?.let {
        apiViewModel.addProjectRoles(
            projectViewModel.projectName,
            projectViewModel.projectDescription,
            it,
            apiViewModel.userRoles.value,
        )
    }
}

fun editOrAdd(
    apiViewModel: ProjectsAPIViewModel,
    projectViewModel: ProjectViewModel,
    apiUser: ApiViewModel
) {

    val projectPost = apiUser.currentUser?.let {
        ProjectPost(
            name = projectViewModel.projectName,
            description = projectViewModel.projectDescription,
            it
        )
    }

    if (projectViewModel.isEdit) {
        projectViewModel.expandedId?.let { projectId ->
            if (projectPost != null) {
                apiViewModel.updateProjectById(projectId, projectPost)
            }

            apiViewModel.deleteProjectUsersByProjectId(projectId)

            val userRoles = apiViewModel.userRoles.value
            userRoles.forEach { (userId, roleIds) ->
                roleIds.forEach { roleId ->
                    apiViewModel.addProjectUser(projectId, userId, roleId)
                }
            }
        }
    } else {
        fetchProject(apiViewModel, projectViewModel, apiUser = apiUser)
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
fun LoginButton(viewModel: ViewModel) {
    FloatingActionButton(
        onClick = {
            viewModel.isRegister = false
        },
        modifier = Modifier
            .height(70.dp)
            .width(120.dp)
            .padding(20.dp),
        shape = RoundedCornerShape(10.dp),
        contentColor = colorResource(R.color.button_description),
        containerColor = colorResource(R.color.button_background)
    ) {
        Text(
            text = "Login:",
            textAlign = TextAlign.Center, // Wyśrodkowanie tekstu
            modifier = Modifier
                .fillMaxWidth() // Wypełnia całą szerokość dostępnego miejsca
                .padding(bottom = 4.dp), // Dodanie paddingu u dołu
            color = backgroundDialog,
            fontFamily = FontFamily(
                Font(R.font.font)
            )
        )
    }
}


@Composable
fun RegisterButton(viewModel: ViewModel) {
    FloatingActionButton(
        onClick = {
            viewModel.isRegister = true
        },
        modifier = Modifier
            .height(70.dp)
            .width(120.dp)
            .padding(20.dp),
        shape = RoundedCornerShape(10.dp),
        contentColor = colorResource(R.color.button_description),
        containerColor = colorResource(R.color.button_background)
    ) {
        Text(
            text = "Register:",
            textAlign = TextAlign.Center, // Wyśrodkowanie tekstu
            modifier = Modifier
                .fillMaxWidth() // Wypełnia całą szerokość dostępnego miejsca
                .padding(bottom = 4.dp), // Dodanie paddingu u dołu
            color = backgroundDialog,
            fontFamily = FontFamily(
                Font(R.font.font)
            )
        )
    }
}


@Composable
fun Submit(
    apiviewModel: ApiViewModel,
    viewModel: ViewModel,
    onNavigate: () -> Unit
) {
    FloatingActionButton(
        onClick = {
            if (viewModel.isRegister) {
                val user = UserRegister(
                    viewModel.email,
                    viewModel.username,
                    viewModel.lastName,
                    viewModel.password,
                    viewModel.firstName
                )
                apiviewModel.registerUser(user)
            } else {
                val login = UserLogin(viewModel.username, viewModel.password)
                apiviewModel.loginUser(user = login)
            }

            apiviewModel.token?.let {
                viewModel.logged = true
            }

            if (viewModel.logged) {
                onNavigate()
                viewModel.loggedUser = viewModel.username
            }

            viewModel.onDismiss()
        },
        modifier = Modifier
            .height(70.dp)
            .width(150.dp)
            .padding(20.dp),
        shape = RoundedCornerShape(10.dp),
        contentColor = colorResource(R.color.button_description),
        containerColor = colorResource(R.color.button_background)
    ) {
        Text(
            text = "Zatwierdź",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            color = backgroundDialog,
            fontFamily = FontFamily(
                Font(R.font.font)
            )
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
    projectViewModel: ProjectViewModel, apiViewModel: ProjectsAPIViewModel
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
    taskId: Int, apiTaskViewModel: ApiTaskViewModel
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
fun ProjectsTaskBtn(
    onNavigate: () -> Unit
) {
    FloatingActionButton(
        onClick = onNavigate,
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