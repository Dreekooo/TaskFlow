package com.example.taskflow.Tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import com.example.taskflow.addProject.ProjectViewModel
import com.example.taskflow.addProject.ProjectsAPIViewModel
import com.example.taskflow.buttons.CloseButton
import com.example.taskflow.buttons.CustomRadioButton
import com.example.taskflow.buttons.SubmitButton
import com.example.taskflow.ui.theme.backgroundDialog
import com.example.taskflow.ui.theme.iconColor
import com.example.taskflow.ui.theme.important
import com.example.taskflow.ui.theme.less
import com.example.taskflow.ui.theme.normal
import com.example.taskflow.ui.theme.radioButton
import com.example.taskflow.ui.theme.textColor
import com.example.taskflow.ui.theme.textEdit


@Composable
fun addTaskDialog(
    taskViewModel: taskViewModel,
    apiViewModel: ProjectsAPIViewModel
) {
    if (taskViewModel.isDialogShow) {
        Dialog(
            onDismissRequest = {
                taskViewModel.onDismissRequest()
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, end = 40.dp)
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
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(top = 15.dp, bottom = 15.dp),
                    ) {
                        Spacer(modifier = Modifier.padding(5.dp))
                        TaskName(taskViewModel = taskViewModel)
                        Spacer(modifier = Modifier.padding(5.dp))
                        TaskDescription(taskViewModel = taskViewModel)
                        Spacer(modifier = Modifier.padding(5.dp))
                        TaskTypeCheckList(
                            taskViewModel()
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        AllUsers(viewModel = apiViewModel, apiTaskViewModel = ApiTaskViewModel())
                        Spacer(modifier = Modifier.padding(5.dp))
                        ButtonsProjects(taskViewModel = taskViewModel, apiViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun TaskName(
    taskViewModel: taskViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Text(
        text = "task name:",
        textAlign = TextAlign.Left,
        modifier = Modifier.padding(start = 20.dp, bottom = 4.dp),
        color = textColor,
        fontFamily = FontFamily(
            Font(R.font.font)
        )
    )
    OutlinedTextField(
        value = taskViewModel.taskTitle,
        onValueChange = {
            taskViewModel.taskTitle = it
        },
        textStyle = TextStyle(textAlign = TextAlign.Justify),
        modifier = Modifier
            .size(500.dp, 50.dp)
            .padding(start = 17.dp, end = 25.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 2.dp, color = textEdit, shape = RoundedCornerShape(10.dp)
            ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = textEdit,
            unfocusedBorderColor = textEdit,
            focusedContainerColor = textEdit,
            unfocusedContainerColor = textEdit,
            focusedTextColor = textColor,
            unfocusedTextColor = textColor
        )
    )
}


@Composable
fun TaskDescription(taskViewModel: taskViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Text(
        text = "description:",
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
            value = taskViewModel.descriptionTask,
            onValueChange = { taskViewModel.descriptionTask = it },
            textStyle = TextStyle(
                textAlign = TextAlign.Start, fontSize = 14.sp
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
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor
            )
        )
    }
}


@Composable
fun AllUsers(viewModel: ProjectsAPIViewModel, apiTaskViewModel: ApiTaskViewModel) {
    val users = viewModel.allUsers.collectAsState().value
    val selectedUsers by apiTaskViewModel.selectedUsers.collectAsState()

    Text(
        text = "add user to the task:",
        textAlign = TextAlign.Left,
        modifier = Modifier.padding(start = 20.dp, bottom = 4.dp),
        color = textColor,
        fontFamily = FontFamily(
            Font(R.font.font)
        )
    )

    LazyColumn(
        modifier = Modifier
            .padding(start = 15.dp, top = 5.dp)
            .height(50.dp)
            .fillMaxWidth()
    ) {
        items(users) { user ->
            Column(modifier = Modifier.padding(bottom = 5.dp)) {
                CustomRadioButton(
                    title = user.username,
                    id = user.id,
                    color = radioButton,
                    isSelected = selectedUsers.contains(user.id),
                    onSelectionChanged = { selectedUserId ->
                        apiTaskViewModel.onSelectionChanged(selectedUserId)
                    },
                    textSize = 18.dp,
                    iconSize = 30.dp
                )
            }
        }
    }
}

@Composable
fun TaskTypeCheckList(
    taskViewModel: taskViewModel
) {
    val taskTypes = listOf(
        Triple("Important", 1, important),
        Triple("Normal", 2, normal),
        Triple("Less Important", 3, less)
    )

    Text(
        text = "type task:",
        textAlign = TextAlign.Left,
        modifier = Modifier.padding(start = 20.dp, bottom = 4.dp),
        color = textColor,
        fontFamily = FontFamily(
            Font(R.font.font)
        )
    )
    Column(
        modifier = Modifier.padding(start = 20.dp)
    ) {
        taskTypes.forEach { (title, id, color) ->
            CustomRadioButton(
                id = id,
                title = title,
                color = color,
                isSelected = taskViewModel.selectedTaskType == id,
                onSelectionChanged = { selectedID ->
                    taskViewModel.selectedTaskType =
                        if (taskViewModel.selectedTaskType == selectedID) null else selectedID
                },
                iconSize = 30.dp,
                textSize = 15.dp
            )
        }
    }
}

@Composable
fun ButtonsProjects(taskViewModel: taskViewModel, apiViewModel: ProjectsAPIViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CloseButton(taskViewModel)
        SubmitButton(ProjectViewModel(), apiViewModel)
    }
}