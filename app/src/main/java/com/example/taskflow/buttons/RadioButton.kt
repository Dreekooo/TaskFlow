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
import com.example.taskflow.ui.theme.radioButton
import com.example.taskflow.ui.theme.textColor

@Composable
fun CustomRadioButton(
    title: String,
    userId: Int,
    color: Color,
    viewModel: ProjectViewModel,
    selectedValues: List<Int>,
    onSelectionChanged: (Int) -> Unit,
) {
    val isChecked = userId in selectedValues

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onSelectionChanged(userId)
            viewModel.enabled = !viewModel.enabled
        }
    ) {
        IconToggleButton(
            checked = isChecked,
            onCheckedChange = {
                onSelectionChanged(userId)
            }
        ) {
            Icon(
                imageVector = if (isChecked) Icons.Rounded.Circle else Icons.Rounded.RadioButtonUnchecked,
                contentDescription = "Radio button icon",
                tint = color,
                modifier = Modifier.size(30.dp)
            )

            if (isChecked) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Selected checkmark",
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
                viewModel.enabled = true
            }
        }

        Text(
            text = title,
            color = textColor,
            fontSize = 18.sp,
            fontFamily = FontFamily(
                Font(R.font.font)
            ),
        )
    }
}

