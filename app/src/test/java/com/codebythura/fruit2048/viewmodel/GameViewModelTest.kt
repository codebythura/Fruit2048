package com.codebythura.fruit2048.viewmodel

import app.cash.turbine.test
import com.codebythura.fruit2048.data.Direction
import com.codebythura.fruit2048.data.EMPTY_GRID
import com.codebythura.fruit2048.data.GRID_SIZE
import com.codebythura.fruit2048.data.GameEngineImpl
import com.codebythura.fruit2048.data.GameEngineState
import com.codebythura.fruit2048.data.GridData
import com.codebythura.fruit2048.database.GameStateEntity
import com.codebythura.fruit2048.ui.game.GameViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.seconds


private val dummyGameStateEntities = listOf(
    GameStateEntity(
        id = 0,
        state = EMPTY_GRID,
        score = 0,
        isOver = false,
    ),
    GameStateEntity(
        id = 1,
        state = listOf(
            listOf(null, GridData(value = 2),GridData(value = 2),null),
            listOf(GridData(value = 2),GridData(value = 2),GridData(value = 4),null),
            listOf(null, GridData(value = 2),GridData(value = 2),null),
            List(GRID_SIZE) { null }
        ),
        score = 0,
        isOver = false,
    ),
)


class GameViewModelTest {


    @get:Rule
    private val mainDispatcherRule = MainDispatcherRule()

    private lateinit var gameViewModel: GameViewModel
    private lateinit var gameStateRepository: FakeGameStateRepository
    private lateinit var gameDataStoreRepository: FakeGameDataStoreRepository
    private lateinit var gameEngine : GameEngineImpl

    @Before
    fun setup() {
        gameStateRepository = FakeGameStateRepository()
        gameDataStoreRepository = FakeGameDataStoreRepository()
        gameEngine = GameEngineImpl()
        gameViewModel = GameViewModel(
            gameStateRepo = gameStateRepository,
            dataStoreRepo = gameDataStoreRepository,
            gameEngine = gameEngine
        )
    }

    @Test
    fun `check game over and show new score dialog`() = runTest {

        gameDataStoreRepository.resetBestScore()
        gameEngine.startGame(
            GameEngineState(
                gridData = listOf(
                    listOf(GridData(value = 2),GridData(value = 4),GridData(value = 2),GridData(value = 4)),
                    listOf(GridData(value = 4),GridData(value = 2),GridData(value = 4),GridData(value = 2)),
                    listOf(GridData(value = 32),GridData(value = 4),GridData(value = 2),GridData(value = 256)),
                    listOf(GridData(value = 256),GridData(value = 128),GridData(value = 16),GridData(value = 16)),
                ),
                score = 0,
                isOver = false,
            )
        )

        gameViewModel.onSwipe(Direction.RIGHT)
        gameViewModel.uiState.test {

            var current = awaitItem()
            while(current.score != 32 && !current.isOver) { current = awaitItem() }
            assertEquals(32,current.score)
            assertTrue(current.isOver)
            assertEquals(0,gameEngine.gameState.value.gridData.flatten().count { it == null })

            gameViewModel.onGameOverDialogDismiss()

            current = awaitItem()
            while(!current.dialogState.showNewScoreDialog) { current = awaitItem() }
            assertTrue(current.dialogState.showNewScoreDialog)

        }
    }

    @Test
    fun `start game engine with a state, swipe left and check the result`() = runTest {
        gameStateRepository.deleteAllStates()
        gameEngine.startGame(
            GameEngineState(
                gridData = listOf(
                    listOf(GridData(value = 2),GridData(value = 2),null,null),
                    List(GRID_SIZE) { null },
                    List(GRID_SIZE) { null },
                    List(GRID_SIZE) { null },
                ),
                score = 0,
                isOver = false,
            )
        )
        gameViewModel.onSwipe(Direction.LEFT)
        gameViewModel.uiState.test(timeout = 2.seconds) {
            var current = awaitItem()
            while (current.score != 4) { current = awaitItem() }
            assertEquals(4, current.score)
            assertEquals(2, gameStateRepository.getRowCount())
        }
    }

    @Test
    fun `check game start up with resume state and check undo is enabled`() = runTest {

        gameStateRepository.setSavedStates(dummyGameStateEntities)
        gameViewModel.onResumeGame()
        gameViewModel.uiState.test(timeout = 2.seconds) {
            var current = awaitItem()
            while(!current.undoEnabled) {
                current = awaitItem()
            }
            assertTrue(current.undoEnabled)
        }

    }

    @Test
    fun `check game start up with resume state and check showResumeDialog is true`() = runTest {
        gameStateRepository.setSavedStates(dummyGameStateEntities)
        gameViewModel.onResumeGame()
        gameViewModel.uiState.test(timeout = 2.seconds) {
            var current = awaitItem()
            while (!current.dialogState.showResumeGameDialog) { current = awaitItem() }
            assertTrue(current.dialogState.showResumeGameDialog)
        }
    }

    @Test
    fun `check game startup with initial game state`() = runTest {
        assertFalse(gameViewModel.uiState.value.undoEnabled)
        assertFalse(gameViewModel.uiState.value.dialogState.showResumeGameDialog)
        assertEquals(2,gameEngine.gameState.value.gridData.flatten().count { it != null })
    }


}

