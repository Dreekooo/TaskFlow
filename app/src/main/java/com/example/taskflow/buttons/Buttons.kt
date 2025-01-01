package com.example.taskflow.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.PersonAddAlt1
import androidx.compose.material.icons.rounded.Task
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.example.taskflow.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.taskflow.addProject.ProjectViewModel
import com.example.taskflow.addProject.ProjectsAPIViewModel

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
fun SubmitButton(
    projectViewModel: ProjectViewModel, apiViewModel: ProjectsAPIViewModel
) {
    FloatingActionButton(
        onClick = {
            fetchProject(projectViewModel = projectViewModel, apiViewModel = apiViewModel)
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
    apiViewModel.addProjectRoles(
        projectViewModel.projectName,
        projectViewModel.projectDescription,
        1,
        apiViewModel.userRoles.value
    )
    projectViewModel.onDismissRequest()
}

@Composable
fun AddRoleButton(
    apiViewModel: ProjectsAPIViewModel,
    viewModel: ProjectViewModel
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
    apiViewModel: ProjectsAPIViewModel,
    projectID: Int
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
) {
    FloatingActionButton(
        onClick = {

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
