package com.example.taskflow.addProject

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Task
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.example.taskflow.R
import com.example.taskflow.buttons.CloseButton
import com.example.taskflow.buttons.CustomRadioButton
import com.example.taskflow.buttons.SubmitButton
import com.example.taskflow.ui.theme.backgroundDialog
import com.example.taskflow.ui.theme.iconColor
import com.example.taskflow.ui.theme.placeholder
import com.example.taskflow.ui.theme.radioButton
import com.example.taskflow.ui.theme.textColor
import com.example.taskflow.ui.theme.textEdit


@Composable
fun AddProjectDialog(
    projectsViewModel: ProjectViewModel,
    apiViewModel: ProjectsAPIViewModel
) {
    if (projectsViewModel.isDialogShow) {
        Dialog(
            onDismissRequest = {
                projectsViewModel.onDismissRequest()
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Task,
                    contentDescription = "",
                    tint = iconColor,
                    modifier = Modifier
                        .size(45.dp)
                        .align(Alignment.TopEnd)
                        .padding(top = 25.dp)
                        .zIndex(2f)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 15.dp)
                        .zIndex(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = backgroundDialog
                    )
                ) {
                    Column(modifier = Modifier.padding(top = 25.dp, bottom = 15.dp)) {
                        ProjectName(projectsViewModel)
                        Spacer(modifier = Modifier.padding(5.dp))
                        projectDescription(projectsViewModel)
                        Spacer(modifier = Modifier.padding(5.dp))
                        RoleName(projectsViewModel = projectsViewModel)
                        Spacer(modifier = Modifier.padding(5.dp))
                        AllUsers(apiViewModel = apiViewModel, projectsViewModel = projectsViewModel)
                        Spacer(modifier = Modifier.padding(5.dp))
                        ButtonsProjects(viewModel = projectsViewModel, apiViewModel)
                    }
                }
            }
        }
    }
}


@Composable
fun ProjectName(
    projectsViewModel: ProjectViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Text(
        text = "project name:",
        textAlign = TextAlign.Left,
        modifier = Modifier.padding(start = 20.dp, bottom = 4.dp),
        color = textColor,
        fontFamily = FontFamily(
            Font(R.font.font)
        )
    )
    OutlinedTextField(
        value = projectsViewModel.projectName,
        onValueChange = {
            projectsViewModel.projectName = it
        },
        textStyle = TextStyle(textAlign = TextAlign.Justify),
        modifier = Modifier
            .size(500.dp, 50.dp)
            .padding(start = 17.dp, end = 25.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 2.dp,
                color = textEdit,
                shape = RoundedCornerShape(10.dp)
            ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() }
        ),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = textEdit,
            unfocusedBorderColor = textEdit,
            focusedContainerColor = textEdit,
            unfocusedContainerColor = textEdit
        )
    )
}


@Composable
fun projectDescription(projectsViewModel: ProjectViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Text(
        text = "description name:",
        textAlign = TextAlign.Left,
        modifier = Modifier.padding(start = 20.dp, bottom = 4.dp),
        color = textColor,
        fontFamily = FontFamily(
            Font(R.font.font)
        )
    )
    Box(
        modifier = Modifier
            .padding(start = 17.dp, end = 25.dp)
            .background(color = textEdit, shape = RoundedCornerShape(16.dp)),
    ) {
        Icon(
            imageVector = Icons.Rounded.Description,
            contentDescription = stringResource(R.string.plus_content),
            tint = iconColor,
            modifier = Modifier
                .size(35.dp)
                .align(Alignment.TopStart)
                .padding(8.dp)
                .zIndex(1f)
        )

        OutlinedTextField(
            value = projectsViewModel.projectDescription,
            onValueChange = { projectsViewModel.projectDescription = it },
            textStyle = TextStyle(
                textAlign = TextAlign.Start,
                fontSize = 14.sp
            ),
            modifier = Modifier
                .size(500.dp, 250.dp)
                .clip(RoundedCornerShape(10.dp))
                .padding(start = 20.dp)
                .fillMaxWidth()
                .zIndex(0f),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            )
        )
    }
}

@Composable
fun RoleName(
    projectsViewModel: ProjectViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Text(
        text = "role name:",
        textAlign = TextAlign.Left,
        modifier = Modifier.padding(start = 20.dp, bottom = 4.dp),
        color = textColor,
        fontFamily = FontFamily(
            Font(R.font.font)
        )
    )
    OutlinedTextField(
        value = projectsViewModel.roleName,
        onValueChange = {
            projectsViewModel.roleName = it
        },
        textStyle = TextStyle(textAlign = TextAlign.Justify),
        modifier = Modifier
            .size(500.dp, 50.dp)
            .padding(start = 17.dp, end = 25.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 2.dp,
                color = textEdit,
                shape = RoundedCornerShape(10.dp)
            ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() }
        ),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = textEdit,
            unfocusedBorderColor = textEdit,
            focusedContainerColor = textEdit,
            unfocusedContainerColor = textEdit
        )
    )
}


@Composable
fun AllUsers(
    apiViewModel: ProjectsAPIViewModel,
    projectsViewModel: ProjectViewModel
) {

    val users = apiViewModel.allUsers.collectAsState().value
    val selectedUsers by apiViewModel.selectedUsers.collectAsState()
    Text(
        text = "add users to project:",
        textAlign = TextAlign.Left,
        modifier = Modifier.padding(top = 5.dp, start = 20.dp, bottom = 4.dp),
        color = textColor,
        fontFamily = FontFamily(
            Font(R.font.font)
        )
    )

    LazyColumn(modifier = Modifier.padding(start = 15.dp, top = 5.dp)) {
        items(users) { user ->
            CustomRadioButton(
                title = user.username,
                color = radioButton,
                userId = user.id,
                viewModel = projectsViewModel,
                selectedValues = selectedUsers,
                onSelectionChanged = { selectedUser ->
                    apiViewModel.onSelectionChanged(selectedUser)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ButtonsProjects(viewModel: ProjectViewModel, apiViewModel: ProjectsAPIViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CloseButton(viewModel)
        SubmitButton(viewModel, apiViewModel)
    }
}