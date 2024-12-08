package com.example.taskflow.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.R
import com.example.taskflow.addProject.ProjectViewModel
import com.example.taskflow.addProject.ProjectsAPIViewModel
import com.example.taskflow.ui.theme.iconColor

@Composable
fun CustomRadioButton(
    title: String,
    color: Color,
    viewModel: ProjectViewModel,
    currentValue: String,
    onSelectionChanged: (String) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            viewModel.enabled = !viewModel.enabled
        }
    ) {
        IconToggleButton(
            checked = currentValue == title,
            onCheckedChange = { isChecked ->
                if (isChecked) {
                    onSelectionChanged(title)
                }
            },
        ) {
            Icon(
                imageVector = if (currentValue == title) Icons.Rounded.Circle else Icons.Rounded.RadioButtonUnchecked,
                contentDescription = "Radio button icon",
                tint = color,
                modifier = Modifier.size(30.dp)
            )
            if (currentValue == title) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Radio button",
                    tint = iconColor,
                )
                viewModel.enabled = true
            }
        }
        Text(
            text = title,
            color = MaterialTheme.colorScheme.tertiaryContainer,
            fontSize = 18.sp,
            fontFamily = FontFamily(
                Font(R.font.font)
            ),
        )
    }
}