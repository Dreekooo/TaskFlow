import android.inputmethodservice.Keyboard.Row
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.taskflow.R
import com.example.taskflow.User.ViewModel
import com.example.taskflow.buttons.LoginButton
import com.example.taskflow.buttons.RegisterButton
import com.example.taskflow.buttons.Submit
import com.example.taskflow.ui.theme.backgroundDialog
import com.example.taskflow.ui.theme.textColor
import com.example.taskflow.ui.theme.textEdit

@Composable
fun LoginForm(
    viewModel: ViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundDialog),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {

        val keyboardController = LocalSoftwareKeyboardController.current
        Text(
            text = "Login:",
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(start = 20.dp, bottom = 4.dp),
            color = textColor,
            fontFamily = FontFamily(
                Font(R.font.font)
            )
        )
        OutlinedTextField(
            value = "",
            onValueChange = {

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
        Text(
            text = "haslo:",
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(start = 20.dp, bottom = 4.dp),
            color = textColor,
            fontFamily = FontFamily(
                Font(R.font.font)
            )
        )
        OutlinedTextField(
            value = "",
            onValueChange = {

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
        Row {
            LoginButton()
            RegisterButton()
            Submit()
        }
    }
}
