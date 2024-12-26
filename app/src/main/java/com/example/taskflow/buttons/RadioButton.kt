package com.example.taskflow.buttons

import android.util.Log
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
import androidx.compose.ui.zIndex
import com.example.taskflow.R
import com.example.taskflow.addProject.ProjectViewModel
import com.example.taskflow.addProject.ProjectsAPIViewModel
import com.example.taskflow.ui.theme.backgroundDialog
import com.example.taskflow.ui.theme.iconColor
import com.example.taskflow.ui.theme.radioButton
import com.example.taskflow.ui.theme.textColor

@Composable
fun CustomRadioButton(
    title: String,
    id: Int,
    color: Color,
    isSelected: Boolean,
    onSelectionChanged: (Int) -> Unit
) {

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onSelectionChanged(id) }
    ) {
        IconToggleButton(
            checked = isSelected,
            onCheckedChange = { onSelectionChanged(id) }
        ) {
            Icon(
                imageVector = if (isSelected) Icons.Rounded.Circle else Icons.Rounded.RadioButtonUnchecked,
                contentDescription = "Radio button icon",
                tint = radioButton,
                modifier = Modifier.size(30.dp)
            )

            if (isSelected) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Selected checkmark",
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Text(
            text = title,
            color = textColor,
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.font))
        )
    }
}


