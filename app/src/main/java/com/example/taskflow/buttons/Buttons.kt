package com.example.taskflow.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.example.taskflow.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.taskflow.addProject.ProjectViewModel

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
    projectViewModel: ProjectViewModel
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
            Icons.Rounded.Check,
            contentDescription = "",
            modifier = Modifier.size(34.dp)
        )
    }
}

@Composable
fun AddProjectButton(projectViewModel: ProjectViewModel) {
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
