package com.example.fakecryptoinvestorremake.presentation.view_edit_investment.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.example.fakecryptoinvestorremake.theme.Grey666
import com.example.fakecryptoinvestorremake.theme.WhiteSoft

@Composable
fun TransparentHintTextField(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    isHintVisible: Boolean = true,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    onFocusChange: (FocusState) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Box(
        modifier = modifier,
    ) {
        val focusManager = LocalFocusManager.current
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = textStyle,
            modifier = Modifier
                .onFocusChanged {
                    onFocusChange(it)
                }
                .fillMaxWidth(),
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
        if (isHintVisible) {
            Text(
                text = hint,
                style = textStyle,
                color = Grey666
            )
        }
    }
}