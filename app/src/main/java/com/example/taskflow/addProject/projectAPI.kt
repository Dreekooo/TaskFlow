package com.example.taskflow.addProject

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.R

@Composable
fun ProjectList(
    viewModel: ProjectsViewModel,
    modifier: Modifier = Modifier,
) {
    val projects by viewModel.projects.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                AddProjectButton()
            }
        }
    ) { innerPadding ->
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
                        viewModel.fetchUserById(project.created_by) { user ->
                            userName = user.username
                        }
                    }

                    projectView(
                        projectName = project.name,
                        created_by = userName,
                        colorResource(R.color.project_color)
                    )

                    Log.d(
                        "API",
                        "Project ID: ${project.id}, Name: ${project.name}, Created By: $userName"
                    )
                }
            }
        }

    }
}

@Composable
fun projectView(
    projectName: String,
    created_by: String,
    color: Color
) {

    Column(
        modifier = Modifier
            .width(346.dp)
            .padding(20.dp)
            .border(width = 3.dp, color = color, shape = RoundedCornerShape(15.dp))
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 800,
                    easing = FastOutSlowInEasing
                )
            )
            .clickable {

            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 15.dp,
                    bottom = 5.dp
                )
                .fillMaxWidth()
                .align(Alignment.Start),
        ) {
            Text(
                text = projectName,
                textAlign = TextAlign.Start,
                fontFamily = FontFamily(
                    Font(R.font.font_bold)
                ),
                fontSize = 22.sp,
                style = TextStyle(fontWeight = FontWeight.ExtraBold)
            )
            Text(
                text = "created by: " + created_by,
                fontFamily = FontFamily(
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
                    start = 16.dp,
                    end = 16.dp,
                    top = 10.dp,
                    bottom = 5.dp
                )
                .align(Alignment.Start),
        ) {
            Text(
                text = "start date: 12.04.2024",
                fontFamily = FontFamily(
                    Font(R.font.font)
                ),
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Text(
                text = "end time: in progress",
                fontFamily = FontFamily(
                    Font(R.font.font)
                ),
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }
}


@Composable
fun AddProjectButton() {
    FloatingActionButton(
        onClick = {
        },
        modifier = Modifier.size(70.dp),
        shape = CircleShape,
        contentColor = colorResource(R.color.button_description),
        containerColor = colorResource(R.color.button_background)
    ) {
        androidx.compose.material3.Icon(
            Icons.Rounded.Add,
            contentDescription = stringResource(R.string.plus_content),
            modifier = Modifier.size(34.dp)
        )
    }
}


