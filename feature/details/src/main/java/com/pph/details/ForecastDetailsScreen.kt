package com.pph.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pph.details.component.ForecastDetailContent
import com.pph.details.state.ForecastDetailScreenState
import com.pph.uicomponents.components.LoadingComponent
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.pph.uicomponents.theme.DemoAppTheme

@Composable
fun ForecastDetailScreen(
    state: ForecastDetailScreenState,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(DemoAppTheme.dimens.x400)
    ) {
        when {
            state.isLoading -> LoadingComponent()
            state.detail != null -> ForecastDetailContent(forecast = state.detail)

            else -> Text(
                text = stringResource(id = R.string.forecast_detail_empty),
                style = DemoAppTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
