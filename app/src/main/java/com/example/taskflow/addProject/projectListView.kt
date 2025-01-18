package com.example.taskflow.addProject

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.R
import com.example.taskflow.buttons.AddProjectButton
import com.example.taskflow.buttons.DeleteButton
import com.example.taskflow.buttons.EditButton
import com.example.taskflow.buttons.ProjectsTaskBtn

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProjectList(
    apiViewModel: ProjectsAPIViewModel,
    modifier: Modifier = Modifier,
    projectViewModel: ProjectViewModel
) {
    val projects by apiViewModel.projects.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            AddProjectButton(
                projectViewModel,
            )
        }
    }) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (projects.isEmpty()) {
                item { Text(text = "No projects available") }
            } else {
                items(projects) { project ->
                    var userName by remember { mutableStateOf("") }

                    LaunchedEffect(project.created_by) {
                        apiViewModel.fetchUserById(project.created_by) { user ->
                            userName = user.username
                        }
                    }

                    val formattedDate =
                        project.deadline?.let { projectViewModel.formatTimestamp(it) }
                            ?: "Nieznana data"

                    ProjectView(
                        projectID = project.id,
                        projectName = project.name,
                        created_by = userName,
                        startDate = formattedDate,
                        colorResource(R.color.project_color),
                        isExpanded = projectViewModel.expandedId == project.id,
                        onClick = {
                            projectViewModel.expandedId =
                                if (projectViewModel.expandedId == project.id) null else project.id
                        },
                        apiViewModel = apiViewModel,
                        projectViewModel = projectViewModel,
                    )

                    Log.d(
                        "API",
                        "Project ID: ${project.id}, Name: ${project.name}, Created By: $userName"
                    )
                }
            }
        }

    }

    /*
    This is a dialog to add new Project
     */
    AddProjectDialog(
        projectsViewModel = projectViewModel,
        apiViewModel = apiViewModel,
    )
}

@Composable
fun ProjectView(
    projectID: Int,
    projectName: String,
    created_by: String,
    startDate: String,
    color: Color,
    isExpanded: Boolean,
    onClick: () -> Unit,
    apiViewModel: ProjectsAPIViewModel,
    projectViewModel: ProjectViewModel
) {
    Column(
        modifier = Modifier
            .width(346.dp)
            .padding(20.dp)
            .border(width = 3.dp, color = color, shape = RoundedCornerShape(15.dp))
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 800, easing = FastOutSlowInEasing
                )
            )
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = 16.dp, end = 16.dp, top = 15.dp, bottom = 5.dp
                )
                .fillMaxWidth()
                .align(Alignment.Start),
        ) {
            Text(
                text = projectName, textAlign = TextAlign.Start, fontFamily = FontFamily(
                    Font(R.font.font_bold)
                ), fontSize = 22.sp, style = TextStyle(fontWeight = FontWeight.ExtraBold)
            )
            Text(
                text = "created by: $created_by", fontFamily = FontFamily(
                    Font(R.font.font)
                )
            )
        }
        Row(
            modifier = Modifier
                .border(2.dp, color, shape = RoundedCornerShape(8.dp))
                .fillMaxWidth(0.9f)
                .padding(2.dp),
        ) {}

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp, end = 16.dp, top = 10.dp, bottom = 5.dp
                )
                .align(Alignment.Start),
        ) {
            Text(
                text = "start date:" + startDate, fontFamily = FontFamily(
                    Font(R.font.font)
                ), fontSize = 14.sp, modifier = Modifier.padding(bottom = 10.dp)
            )

            Text(
                text = "end time: in progress", fontFamily = FontFamily(
                    Font(R.font.font)
                ), fontSize = 14.sp, modifier = Modifier.padding(bottom = 10.dp)
            )
        }

        if (isExpanded) {
            ProjectButtons(
                apiViewModel = apiViewModel,
                projectID = projectID,
                projectViewModel = projectViewModel
            )
        }
    }
}


@Composable
fun ProjectButtons(
    apiViewModel: ProjectsAPIViewModel,
    projectViewModel: ProjectViewModel,
    projectID: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        DeleteButton(
            apiViewModel = apiViewModel,
            projectID = projectID
        )
        ProjectsTaskBtn()
        EditButton(projectViewModel = projectViewModel, apiViewModel = apiViewModel)
    }
}


