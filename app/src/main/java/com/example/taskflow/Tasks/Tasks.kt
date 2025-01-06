package com.example.taskflow.Tasks

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.R
import com.example.taskflow.ui.theme.important
import com.example.taskflow.ui.theme.less
import com.example.taskflow.ui.theme.normal
import java.util.Date

@Composable
fun TaskView(
    taskID: Int,
    taskName: String,
    taskType: Int,
    taskCreated: Date,
    taskEnd: Date,
    isExpanded: Boolean,
    onClick: () -> Unit,
    apiViewModel: ApiTaskViewModel,
    taskViewModel: taskViewModel
) {
    val color = when (taskType) {
        1 -> important
        2 -> normal
        3 -> less
        else -> Color.Gray
    }

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
                text = taskName, textAlign = TextAlign.Start, fontFamily = FontFamily(
                    Font(R.font.font_bold)
                ), fontSize = 22.sp, style = TextStyle(fontWeight = FontWeight.ExtraBold)
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
                text = "start date: " + taskViewModel.formatDate(taskCreated),
                fontFamily = FontFamily(
                    Font(R.font.font)
                ),
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Text(
                text = "end time: " + taskViewModel.formatDate(taskEnd),
                fontFamily = FontFamily(
                    Font(R.font.font)
                ),
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }
}
