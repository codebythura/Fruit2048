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
    val dialogState: GameDialogState = GameDialogState(),
)

data class GameDialogState(
    val showGameOverDialog: Boolean = false,
    val showNewScoreDialog: Boolean = false,
    val showRestartConfirmDialog: Boolean = false,
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
            undoEnabled = stateCount > 0,
            dialogState = dialogState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = GameUIState()
    )

    init {
        startNewGame()
    }

    fun onStartNewGame() {
        if (uiState.value.undoEnabled) {
            _dialogState.update { it.copy(showRestartConfirmDialog = true) }
            return
        }
        startNewGame()
    }

    fun startNewGame() {
        viewModelScope.launch {
            _dialogState.value = GameDialogState()
            gameStateRepo.deleteAllStates()
            gameEngine.startNewGame()
        }
    }

    fun onUndo() {
        viewModelScope.launch {
            gameStateRepo.getAndDeleteLastState()?.let { stateFromDb ->
                gameEngine.undo(stateFromDb.toEngineState())
            }
        }
    }

    fun onSwipe(direction: Direction) {
        viewModelScope.launch {
            gameStateRepo.saveState(gameEngine.gameState.value.toEntity())
            gameEngine.onSwipe(direction)
            _dialogState.update { it.copy(showGameOverDialog = gameEngine.checkIfGameOver()) }
        }
    }

    fun onGameOverDialogDismiss() {
        viewModelScope.launch {
            if(dataStoreRepo.updateBestScore(gameEngine.gameState.value.score)) {
                _dialogState.update {
                    it.copy(
                        showGameOverDialog = false,
                        showNewScoreDialog = true
                    )
                }
            } else {
                startNewGame()
            }
        }
    }

    fun onNewScoreDialogDismiss() {
        _dialogState.update { it.copy(showNewScoreDialog = false) }
        startNewGame()
    }

    fun onRestartGameDialogDismiss() {
        _dialogState.update { it.copy(showRestartConfirmDialog = false) }
    }

}