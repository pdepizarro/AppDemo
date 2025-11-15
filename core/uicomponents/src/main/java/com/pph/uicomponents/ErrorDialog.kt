package com.pph.uicomponents

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun ErrorDialog(
    message: String, onDismiss: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(onClick = onDismiss) {
            Text(stringResource(R.string.accept))
        }
    }, title = { Text(stringResource(R.string.error)) }, text = { Text(message) })
}