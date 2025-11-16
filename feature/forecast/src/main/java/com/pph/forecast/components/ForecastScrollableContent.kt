package com.pph.forecast.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pph.shared.ui.model.DailyForecastUiModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun ForecastScrollableContent(
    forecast: List<DailyForecastUiModel>,
    squareSize: Dp,
    onDayClick: (DailyForecastUiModel) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val isProgrammaticScroll = remember { mutableStateOf(false) }

    // Índice del ítem más cercano al centro del viewport
    val centerItemIndex by remember(listState) {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visible = layoutInfo.visibleItemsInfo
            if (visible.isEmpty()) return@derivedStateOf null

            val viewportCenter =
                (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2

            visible.minByOrNull { item ->
                val itemCenter = item.offset + item.size / 2
                abs(itemCenter - viewportCenter)
            }?.index
        }
    }

    // Item selected
    val selectedForecast: DailyForecastUiModel? =
        centerItemIndex?.let { index ->
            forecast.getOrNull(index)
        }

    // Vertical padding to center first and last items
    val verticalPadding = remember(screenHeight, squareSize) {
        ((screenHeight - squareSize) / 2).coerceAtLeast(0.dp)
    }

    // Scroll state (scrolling or not)
    val isScrolling by snapshotFlow { listState.isScrollInProgress }
        .collectAsState(initial = false)

    val gravityLineFactor by animateFloatAsState(
        targetValue = if (isScrolling) 0.6f else 1f,
        label = "gravityLineAnimation"
    )

    // Magnet effect: when the scroll stops, center the nearest item
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .filter { scrolling -> !scrolling }
            .collect {
                // Si venimos de un scroll programático, evitamos doble imán
                if (isProgrammaticScroll.value) {
                    isProgrammaticScroll.value = false
                    return@collect
                }

                val index = centerItemIndex ?: return@collect
                val layoutInfo = listState.layoutInfo
                val itemInfo = layoutInfo.visibleItemsInfo
                    .firstOrNull { it.index == index } ?: return@collect

                val targetOffset = (itemInfo.size / 2).minus(24)

                listState.animateScrollToItem(
                    index = index,
                    scrollOffset = targetOffset.coerceAtLeast(0)
                )
            }
    }

    // Base width for gravity lines
    val baseLineWidth = remember(screenWidth, squareSize) {
        val centerX = screenWidth / 2
        val cardHalf = squareSize / 2
        val minGapFromCard = 12.dp

        (centerX - cardHalf - minGapFromCard).coerceAtLeast(0.dp)
    }

    Column(Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // Lateral lines
            val lineHeight = 3.dp
            val dynamicLineWidth = baseLineWidth * gravityLineFactor

            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(dynamicLineWidth)
                    .height(lineHeight)
                    .background(Color.White.copy(alpha = 0.4f))
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(dynamicLineWidth)
                    .height(lineHeight)
                    .background(Color.White.copy(alpha = 0.4f))
            )

            // Main list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(
                    top = verticalPadding,
                    bottom = verticalPadding
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                itemsIndexed(
                    items = forecast,
                    key = { _, item -> item.date.toEpochDay() }
                ) { index, dayForecast ->

                    val isSelected = index == centerItemIndex

                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.15f else 0.9f,
                        label = "scaleAnimation"
                    )

                    ForecastDayItem(
                        forecast = dayForecast,
                        squareSize = squareSize,
                        scale = scale,
                        onCenterRequest = {
                            // On card click, center the item with a programmatic scroll
                            scope.launch {
                                val layoutInfo = listState.layoutInfo

                                val itemInfo = layoutInfo.visibleItemsInfo
                                    .firstOrNull { it.index == index }

                                val targetOffset = if (itemInfo != null) {
                                    (itemInfo.size / 2).minus(24)
                                } else {
                                    0
                                }

                                isProgrammaticScroll.value = true
                                listState.animateScrollToItem(
                                    index = index,
                                    scrollOffset = targetOffset.coerceAtLeast(0)
                                )
                            }
                        },
                        onClick = { onDayClick(dayForecast) }
                    )
                }
            }
        }

        Button(
            onClick = { selectedForecast?.let(onDayClick) },
            enabled = selectedForecast != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(16.dp)
        ) {
            Text(text = "Ver detalles del día seleccionado")
        }
    }
}
