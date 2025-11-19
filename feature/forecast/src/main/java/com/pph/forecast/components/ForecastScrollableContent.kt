package com.pph.forecast.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pph.forecast.ForecastViewModel
import com.pph.shared.ui.model.DailyForecastUiModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.abs
import com.pph.forecast.R

@Composable
fun ForecastScrollableContent(
    vm: ForecastViewModel,
    forecast: List<DailyForecastUiModel>,
    squareSize: Dp
) {

    // Screen config
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    // ViewModel state: Collect state and create coroutine scope
    val state by vm.state.collectAsState()
    val scope = rememberCoroutineScope()

    // Snap state
    val isSnapping = remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    var centerItemIndex by remember { mutableStateOf<Int?>(null) }

    // Initial restore: Restore list position only once
    var didRestoreInitialPosition by rememberSaveable { mutableStateOf(false) }
    val listState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }

    // Current selection
    val selectedForecast =
        forecast.getOrNull(centerItemIndex ?: selectedIndex)

    // Scrolling state
    val isScrolling by snapshotFlow { listState.isScrollInProgress }
        .collectAsState(initial = false)

    // Vertical padding
    val verticalPadding = remember(screenHeight, squareSize) {
        ((screenHeight - squareSize) / 2).coerceAtLeast(0.dp)
    }

    // Gravity factor: Target width factor for side lines
    val gravityLineFactorTarget = if (isScrolling) 0.6f else 1f

    // Gravity animation: Animate factor when scroll state changes
    val gravityLineFactor by animateFloatAsState(
        targetValue = gravityLineFactorTarget,
        label = "gravityLineAnimation"
    )

    // Line growth flag: Check if side lines are still animating
    val isAnimatingLinesGrow by remember {
        derivedStateOf {
            !isScrolling && gravityLineFactor < 0.999f
        }
    }

    // Baseline width: Compute base length for side decorative lines
    val baseLineWidth = remember(screenWidth, squareSize) {
        val centerX = screenWidth / 2
        val cardHalf = squareSize / 2
        val minGap = 12.dp

        (centerX - cardHalf - minGap).coerceAtLeast(0.dp)
    }

    // Button state: Enable button only when interaction is safe
    val isButtonEnabled = selectedForecast != null &&
            !isSnapping.value &&
            !isAnimatingLinesGrow &&
            !isScrolling

    // Center detection: Find item closest to viewport center
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val visible = layoutInfo.visibleItemsInfo
                if (visible.isEmpty()) return@collect

                val viewportCenter =
                    (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2

                centerItemIndex = visible.minByOrNull { item ->
                    val center = item.offset + item.size / 2
                    abs(center - viewportCenter)
                }?.index
            }
    }

    // Snap helper: Animate list so item moves to center
    suspend fun snapToCenter(index: Int, offsetPx: Int) {
        if (isSnapping.value) return
        isSnapping.value = true
        try {
            listState.animateScrollToItem(index, offsetPx)
        } finally {
            isSnapping.value = false
        }
    }

    // Initial position: Restore previously selected day on first render
    LaunchedEffect(forecast, squareSize, state.lastSelectedEpochDay) {
        if (didRestoreInitialPosition) return@LaunchedEffect
        if (forecast.isEmpty()) return@LaunchedEffect

        // List readiness: Wait until list has visible items
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.isNotEmpty() }
            .filter { it }
            .first()

        val targetIndex = state.lastSelectedEpochDay?.let { epoch ->
            forecast.indexOfFirst { it.date.toEpochDay() == epoch }
                .takeIf { it >= 0 }
        } ?: 0

        val targetOffsetPx = with(density) {
            (squareSize / 2).coerceAtLeast(0.dp).roundToPx()
        }

        snapToCenter(targetIndex, targetOffsetPx)
        didRestoreInitialPosition = true
    }

    // Auto snap: After scrolling stops, snap nearest item to center
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { scrolling ->
                if (scrolling) return@collect
                if (isSnapping.value) return@collect

                val index = centerItemIndex ?: return@collect

                val itemInfo = listState.layoutInfo.visibleItemsInfo
                    .firstOrNull { it.index == index }
                    ?: return@collect

                val offset = (itemInfo.size / 2).coerceAtLeast(0)

                snapToCenter(index, offset)
            }
    }

    // Selection sync: Keep selectedIndex in sync with centerItemIndex
    LaunchedEffect(centerItemIndex) {
        centerItemIndex?.let { selectedIndex = it }
    }

    Column(Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            // Line size: Set fixed height and animated width
            val lineHeight = 3.dp
            val dynamicWidth = baseLineWidth * gravityLineFactor

            // Left line
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(dynamicWidth)
                    .height(lineHeight)
                    .background(Color.White.copy(alpha = 0.4f))
            )

            // Right line
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(dynamicWidth)
                    .height(lineHeight)
                    .background(Color.White.copy(alpha = 0.4f))
            )

            // Forecast list: Display scrollable list of days
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                userScrollEnabled = !isSnapping.value && !isAnimatingLinesGrow,
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

                    // Check if this item is centered
                    val isSelected = (centerItemIndex ?: selectedIndex) == index

                    // Animate size based on selection
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.15f else 0.9f,
                        label = "scaleAnimation"
                    )

                    ForecastDayItem(
                        forecast = dayForecast,
                        squareSize = squareSize,
                        scale = scale,
                        onCenterRequest = {
                            scope.launch {
                                val info = listState.layoutInfo.visibleItemsInfo
                                    .firstOrNull { it.index == index }

                                val offset = info?.size?.div(2) ?: 0

                                snapToCenter(index, offset)
                            }
                        },
                        onClick = {}
                    )
                }
            }
        }

        // Detail button
        Button(
            onClick = { selectedForecast?.let { vm.onDaySelected(it) } },
            enabled = isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(16.dp)
        ) {
            Text(stringResource(id = R.string.forecast_scrollable_button_details))
        }
    }
}