package com.codebythura.fruit2048.ui.game

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codebythura.fruit2048.data.Direction
import com.codebythura.fruit2048.data.GameEngine
import com.codebythura.fruit2048.data.TileData
import com.codebythura.fruit2048.database.toEngineState
import com.codebythura.fruit2048.repository.DataStoreRepository
import com.codebythura.fruit2048.repository.GameStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@Immutable
data class GameUIState(
    val tileData: List<TileData> = emptyList(),
    val bestScore: Int = 0,
    val score: Int = 0,
    val undoEnabled: Boolean = false,
    val isOver : Boolean = false,
    val dialogState: GameDialogState = GameDialogState(),
)

data class GameDialogState(
    val showNewScoreDialog: Boolean = false,
    val showRestartConfirmDialog: Boolean = false,
    val showResumeGameDialog: Boolean = false,
)

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameStateRepo: GameStateRepository,
    private val dataStoreRepo: DataStoreRepository,
    private val gameEngine: GameEngine,
) : ViewModel() {

    private val _dialogState = MutableStateFlow(GameDialogState())
    val uiState = combine(
        _dialogState,
        gameEngine.gameState,
        gameStateRepo.observeRowCount(),
        dataStoreRepo.observeBestScore(),
    ) { dialogState, gameState, stateCount, bestScore ->
        GameUIState(
            tileData = gameState.toTileData(),
            score = gameState.score,
            bestScore = bestScore,
            undoEnabled = stateCount > 1,
            isOver = gameState.isOver,
            dialogState = dialogState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = GameUIState()
    )

    init { onResumeGame() }

    fun onResumeGame() {
        viewModelScope.launch {
            if(gameStateRepo.getRowCount() > 1) {
                _dialogState.update { it.copy(showResumeGameDialog = true) }
            } else {
                startNewGame(clearDialogs = false)
            }
        }
    }

    fun onRestartGame() {
        if (uiState.value.undoEnabled) {
            _dialogState.update { it.copy(showRestartConfirmDialog = true) }
            return
        }
        startNewGame()
    }

    fun startNewGame(clearDialogs: Boolean = true) {
        viewModelScope.launch {
            if(clearDialogs) _dialogState.value = GameDialogState()
            gameStateRepo.deleteAllStates()
            gameEngine.startGame()
        }
    }

    fun onUndo() {
        viewModelScope.launch {
            gameStateRepo.onUndo()?.let { gameEngine.undo(it.toEngineState()) }
        }
    }

    fun onSwipe(direction: Direction) {

        viewModelScope.launch {

            val previousState = gameEngine.gameState.value
            if (previousState.isOver) return@launch

            val currentRowCount = gameStateRepo.getRowCount()
            if (currentRowCount == 0) { gameStateRepo.saveState(previousState.toEntity()) }

            gameEngine.onSwipe(direction)
            val updatedState = gameEngine.gameState.value

            if (updatedState.gridData != previousState.gridData || updatedState.score != previousState.score) {
                gameStateRepo.saveState(updatedState.toEntity())
            } else if(currentRowCount == 0) {
                gameStateRepo.deleteAllStates()
            }

        }
    }

    fun onGameOverDialogDismiss() {
        viewModelScope.launch {
            dataStoreRepo.updateBestScore(gameEngine.gameState.value.score).let { updated ->
                if(updated) { _dialogState.update { it.copy(showNewScoreDialog = true) } }
                else { startNewGame() }
            }
        }
    }

    fun onNewScoreDialogDismiss() {
        startNewGame()
    }

    fun onRestartGameDialogDismiss() {
        _dialogState.update { it.copy(showRestartConfirmDialog = false) }
    }

    fun onResumeGameDialogDismiss() {
        viewModelScope.launch {
            _dialogState.update { it.copy(showResumeGameDialog = false) }
            gameStateRepo.onResume()?.let { gameEngine.startGame(it.toEngineState()) }
        }
    }

}