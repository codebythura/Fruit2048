package com.codebythura.fruit2048.ui.game.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.codebythura.fruit2048.data.AppConsts
import com.codebythura.fruit2048.data.TileData
import com.codebythura.fruit2048.data.getTile
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun Board(
    modifier: Modifier = Modifier,
    tileDataList: List<TileData> = emptyList(),
    padding: Dp = AppConsts.defaultPaddingDp,
    cornerRadius: Dp = AppConsts.defaultCornerRadius,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    tileBgColor: Color = MaterialTheme.colorScheme.secondaryContainer,
) {

    BoxWithConstraints(
        modifier = modifier
            .boardBackground(
                paddingDp = padding,
                cornerRadius = cornerRadius,
                backgroundColor = backgroundColor,
                tileBgColor = tileBgColor,
            ),
    ) {

        val boardSizeDp = minOf(maxWidth, maxHeight)
        val tileSizeDp = (boardSizeDp - padding * 5) / 4
        fun getOffset(index: Int): Dp = padding * (index + 1) + (tileSizeDp * index)

        for (tileData in tileDataList) {
            key(tileData.id) {
                val animateOffsetX by animateDpAsState(
                    targetValue = getOffset(tileData.col),
                    animationSpec = tween(
                        durationMillis = AppConsts.defaultDuration,
                        easing = FastOutSlowInEasing
                    )
                )
                val animateOffsetY by animateDpAsState(
                    targetValue = getOffset(tileData.row),
                    animationSpec = tween(
                        durationMillis = AppConsts.defaultDuration,
                        easing = FastOutSlowInEasing
                    )
                )
                TileItem(
                    tileData = tileData,
                    cornerRadius = cornerRadius,
                    modifier = Modifier
                        .size(tileSizeDp)
                        .offset {
                            IntOffset(
                                animateOffsetX.roundToPx(),
                                animateOffsetY.roundToPx()
                            )
                        }
                )
            }
        }
    }
}


@Composable
private fun TileItem(
    tileData : TileData,
    cornerRadius: Dp,
    modifier: Modifier = Modifier,
    contentPadding: Dp = 12.dp,
) {

    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val shape = remember(cornerRadius) { RoundedCornerShape(cornerRadius) }

    val scale = remember { Animatable(if (tileData.shouldAnimate) 0f else 1f) }
    LaunchedEffect(tileData.id) {
        if (tileData.shouldAnimate) {
            delay(AppConsts.defaultDuration.milliseconds)
            scale.snapTo(0.8f)
            scale.animateTo(
                targetValue = 1.12f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium,
                ),
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium,
                ),
            )
        }
    }

    val tile = getTile(tileData.value)
    val animatedColor by animateColorAsState(targetValue = tile.color)
    val animatedBorderColor by animateColorAsState(targetValue = tile.borderColor)

    Box(
        modifier =
            modifier
                .aspectRatio(1f)
                .graphicsLayer {
                    this.shape = shape
                    this.clip = true
                    this.scaleX = scale.value
                    this.scaleY = scale.value
                }
                .drawBehind {
                    drawRect(color = animatedColor)
                    drawOutline(
                        color = animatedBorderColor,
                        outline = shape.createOutline(size, layoutDirection, density),
                        style = Stroke(width = 4.dp.toPx())
                    )
                },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Text(
                text = tile.value.toString(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = tile.icon,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}


private fun Modifier.boardBackground(
    paddingDp: Dp,
    cornerRadius: Dp,
    backgroundColor: Color,
    tileBgColor: Color,
) = this then aspectRatio(1f).drawBehind {
    val cornerRadiusPx = cornerRadius.toPx()
    val paddingPx = paddingDp.toPx()
    val boardSizePx = size.minDimension
    val tileSizePx = (boardSizePx - paddingPx * 5) / 4
    drawRoundRect(
        color = backgroundColor,
        cornerRadius = CornerRadius(cornerRadiusPx)
    )
    for (i in 0..3) {
        val offsetY = paddingPx * (i + 1) + (tileSizePx * i)
        for (j in 0..3) {
            val offsetX = paddingPx * (j + 1) + (tileSizePx * j)
            drawRoundRect(
                color = tileBgColor,
                topLeft = Offset(offsetX, offsetY),
                cornerRadius = CornerRadius(cornerRadiusPx),
                size = Size(tileSizePx, tileSizePx)
            )
        }
    }
}