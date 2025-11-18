package com.pph.shared.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit,
    onRetryClick: (() -> Unit)? = null
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f)),
            contentAlignment = Alignment.Center
        ) {

            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .wrapContentHeight()
            ) {

                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Error",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
                        modifier = Modifier.alpha(0.9f)
                    )

                    Spacer(Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {

                        TextButton(onClick = onDismiss) {
                            Text("Cerrar")
                        }

                        onRetryClick?.let { retryFn ->
                            Spacer(Modifier.width(8.dp))

                            Button(onClick = retryFn) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }
        }
    }
}
