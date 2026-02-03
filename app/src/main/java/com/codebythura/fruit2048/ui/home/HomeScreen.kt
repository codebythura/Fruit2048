package com.codebythura.fruit2048.ui.home

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codebythura.fruit2048.data.Direction
import com.codebythura.fruit2048.data.TileData
import com.codebythura.fruit2048.ui.home.components.ActionsRow
import com.codebythura.fruit2048.ui.home.components.AppTitle
import com.codebythura.fruit2048.ui.home.components.Board
import com.codebythura.fruit2048.ui.home.components.GameOverDialog
import com.codebythura.fruit2048.ui.home.components.ScoreRow
import kotlin.math.abs

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    if (state.isGameOver) {
        GameOverDialog(
            score = state.score,
            onGameRestart = viewModel::startNewGame
        )
    }
    Scaffold(
        modifier = modifier,
        content = { innerPadding ->
            HomeContent(
                bestScore = state.bestScore,
                score = state.score,
                tileData = state.tileData,
                onStartNewGame = viewModel::startNewGame,
                onUndo = viewModel::onUndo,
                undoEnabled = state.undoEnabled,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .onSwipe(onSwipe = viewModel::onSwipe)
                    .padding(start = 24.dp, end = 24.dp, top = 64.dp)
            )
        }
    )
}

@Composable
fun HomeContent(
    bestScore : Int,
    score: Int,
    tileData: List<TileData>,
    onStartNewGame: () -> Unit,
    onUndo: () -> Unit,
    undoEnabled : Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppTitle()
        ScoreRow(bestScore = bestScore,score = score)
        ActionsRow(
            undoEnabled = undoEnabled,
            onUndo = onUndo,
            onStartNewGame = onStartNewGame
        )
        Board(tileData = tileData)
    }
}


private fun Modifier.onSwipe(
    threshold: Float = 50f,
    onSwipe: (Direction) -> Unit
): Modifier = this.pointerInput(Unit) {
    var dragAmountX = 0f
    var dragAmountY = 0f
    detectDragGestures(
        onDragStart = {
            dragAmountX = 0f
            dragAmountY = 0f
        },
        onDrag = { change, dragAmount ->
            change.consume()
            dragAmountX += dragAmount.x
            dragAmountY += dragAmount.y
        },
        onDragEnd = {
            val absX = abs(dragAmountX)
            val absY = abs(dragAmountY)

            if (absX > threshold || absY > threshold) {
                if (absX > absY) {
                    // Horizontal Swipe
                    onSwipe(if (dragAmountX > 0) Direction.RIGHT else Direction.LEFT)
                } else {
                    // Vertical Swipe
                    onSwipe(if (dragAmountY > 0) Direction.DOWN else Direction.UP)
                }
            }
        }
    )
}

