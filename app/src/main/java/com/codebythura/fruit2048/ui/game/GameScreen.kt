package com.codebythura.fruit2048.ui.game

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codebythura.fruit2048.R
import com.codebythura.fruit2048.data.AppConsts
import com.codebythura.fruit2048.data.Direction
import com.codebythura.fruit2048.data.TileData
import com.codebythura.fruit2048.ui.game.components.ActionsRow
import com.codebythura.fruit2048.ui.game.components.AppTitle
import com.codebythura.fruit2048.ui.game.components.Board
import com.codebythura.fruit2048.ui.game.dialog.GameOverDialog
import com.codebythura.fruit2048.ui.game.dialog.NewBestScoreDialog
import com.codebythura.fruit2048.ui.game.components.ScoreRow
import com.codebythura.fruit2048.ui.game.dialog.RestartConfirmDialog
import com.codebythura.fruit2048.ui.game.dialog.ResumeGameDialog
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = hiltViewModel(),
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    GameScreenContent(
        state = state,
        onUndo = viewModel::onUndo,
        onSwipe = viewModel::onSwipe,
        onRestartGame = viewModel::onRestartGame,
        startNewGame = viewModel::startNewGame,
        onGameOverDialogDismiss = viewModel::onGameOverDialogDismiss,
        onNewScoreDialogDismiss = viewModel::onNewScoreDialogDismiss,
        onRestartGameDialogDismiss = viewModel::onRestartGameDialogDismiss,
        onResumeGameDialogDismiss = viewModel::onResumeGameDialogDismiss,
        modifier = modifier
    )

}

@Composable
fun GameScreenContent(
    state: GameUIState,
    startNewGame: () -> Unit,
    onUndo: () -> Unit,
    onSwipe: (Direction) -> Unit,
    onRestartGame: () -> Unit,
    onGameOverDialogDismiss: () -> Unit,
    onNewScoreDialogDismiss: () -> Unit,
    onRestartGameDialogDismiss: () -> Unit,
    onResumeGameDialogDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {

    GameDialogEffect(
        bestScore = state.bestScore,
        score = state.score,
        state = state.dialogState,
        isGameOver = state.isOver,
        startNewGame = startNewGame,
        onGameOverDialogDismiss = onGameOverDialogDismiss,
        onRestartGameDialogDismiss = onRestartGameDialogDismiss,
        onNewScoreDialogDismiss = onNewScoreDialogDismiss,
        onResumeGameDialogDismiss = onResumeGameDialogDismiss
    )

    Scaffold(
        modifier = modifier,
        content = { innerPadding ->
            GameContent(
                bestScore = state.bestScore,
                score = state.score,
                tileData = state.tileData,
                onStartNewGame = onRestartGame,
                onUndo = onUndo,
                undoEnabled = state.undoEnabled,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .onSwipe(onSwipe = onSwipe)
                    .padding(start = 24.dp, end = 24.dp, top = 64.dp)
            )
        }
    )

}

@Composable
private fun GameContent(
    score: Int,
    bestScore: Int,
    tileData: List<TileData>,
    onUndo: () -> Unit,
    onStartNewGame: () -> Unit,
    undoEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.testTag(stringResource(R.string.game_content)),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppTitle()
        ScoreRow(bestScore = bestScore, score = score)
        ActionsRow(
            undoEnabled = undoEnabled,
            onUndo = onUndo,
            onStartNewGame = onStartNewGame
        )
        Board(tileDataList = tileData)
    }
}

@Composable
private fun GameDialogEffect(
    score: Int,
    bestScore: Int,
    isGameOver: Boolean,
    state: GameDialogState,
    startNewGame: () -> Unit,
    onGameOverDialogDismiss: () -> Unit,
    onNewScoreDialogDismiss: () -> Unit,
    onRestartGameDialogDismiss: () -> Unit,
    onResumeGameDialogDismiss: () -> Unit,
) {

    var showGameOverDialog by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(isGameOver) {
        if (isGameOver) {
            delay(AppConsts.defaultDuration.milliseconds) // wait for the last tile animation
            showGameOverDialog = true
        } else {
            showGameOverDialog = false
        }
    }
    if (showGameOverDialog) {
        GameOverDialog(
            score = score,
            onGameRestart = onGameOverDialogDismiss
        )
    }
    if (state.showNewScoreDialog) {
        NewBestScoreDialog(
            bestScore = bestScore,
            onDismiss = onNewScoreDialogDismiss
        )
    }
    if (state.showRestartConfirmDialog) {
        RestartConfirmDialog(
            onDismiss = onRestartGameDialogDismiss,
            onConfirm = startNewGame
        )
    }
    if (state.showResumeGameDialog) {
        ResumeGameDialog(
            onResume = onResumeGameDialogDismiss,
            onNewGame = startNewGame
        )
    }
}


private fun Modifier.onSwipe(
    threshold: Float = 50f,
    onSwipe: (Direction) -> Unit,
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

