package com.codebythura.fruit2048.ui.home.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.codebythura.fruit2048.ui.theme.Fruit2048Theme


val defaultPaddingDp = 8.dp
val defaultBoardBgColor = Color(0xFFE8E3E3)
val defaultTileBgColor = Color(0xFFd6d3d1)
val defaultCornerRadius = 12.dp
const val defaultDurationMillis = 200

data class TileData(
    val id: String,
    var value: Int,
    var row: Int,
    var col: Int,
)

@Composable
fun Board(
    tileData: List<TileData> = emptyList(),
    padding: Dp = defaultPaddingDp,
    cornerRadius: Dp = defaultCornerRadius,
    backgroundColor: Color = defaultBoardBgColor,
    tileBgColor: Color = defaultTileBgColor,
    modifier: Modifier = Modifier,
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
        for (tile in tileData) {
            key(tile.id) {
                val animateOffsetX by animateDpAsState(
                    targetValue = getOffset(tile.col),
                    animationSpec = tween(defaultDurationMillis, easing = FastOutSlowInEasing)
                )
                val animateOffsetY by animateDpAsState(
                    targetValue = getOffset(tile.row),
                    animationSpec = tween(defaultDurationMillis, easing = FastOutSlowInEasing)
                )
                TileItem(
                    tileData = tile,
                    modifier = Modifier
                        .offset(animateOffsetX, animateOffsetY)
                        .size(tileSizeDp)
                )
            }
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
    val boardSizePx = minOf(size.width, size.height)
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


@Preview
@Composable
private fun BoardPreview() {

    val tileData = listOf(
        TileData(
            id = "1",
            value = 2,
            row = 2,
            col = 2,
        ),
        TileData(
            id = "2",
            value = 16,
            row = 1,
            col = 3,
        ),
    )
    Fruit2048Theme {
        Board(
            tileData = tileData,
            modifier = Modifier
        )
    }
}