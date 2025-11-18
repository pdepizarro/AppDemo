package com.pph.uicomponents.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pph.uicomponents.theme.DemoAppTheme
import com.pph.uicomponents.R

@Composable
fun ErrorComponent(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = stringResource(id = R.string.error_component_icon_content_description),
                tint = Color(0xFFFFC107),
                modifier = Modifier
                    .size(DemoAppTheme.dimens.x2400)
                    .padding(bottom = DemoAppTheme.dimens.x700)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onRetryClick) {
                Text(text = stringResource(id = R.string.error_component_retry))
            }
        }
    }
}
