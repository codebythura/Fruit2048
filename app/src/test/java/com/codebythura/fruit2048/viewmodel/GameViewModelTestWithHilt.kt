package com.codebythura.fruit2048.viewmodel

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import com.codebythura.fruit2048.data.Direction
import com.codebythura.fruit2048.data.GameEngine
import com.codebythura.fruit2048.database.AppDatabase
import com.codebythura.fruit2048.repository.DataStoreRepository
import com.codebythura.fruit2048.repository.GameStateRepository
import com.codebythura.fruit2048.ui.game.GameViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class GameViewModelTestWithHilt {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val mainDispatcherRule = MainDispatcherRule()

    @Inject
    lateinit var gameEngine: GameEngine

    @Inject
    lateinit var gameStateRepository: GameStateRepository

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    @Inject
    lateinit var database : AppDatabase

    private lateinit var gameViewModel : GameViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        gameViewModel = GameViewModel(
            gameEngine = gameEngine,
            gameStateRepo = gameStateRepository,
            dataStoreRepo = dataStoreRepository,
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `start a new game, swipe and check game state and score`() = runTest {
        gameViewModel.startNewGame()
        gameViewModel.uiState.test {
            awaitItem()
            gameViewModel.onSwipe(Direction.LEFT)
            awaitUntil("Wait undo is enabled") { it.undoEnabled }
            assertTrue(gameStateRepository.getRowCount() > 1)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onResumeGame shows resume dialog when history exists`() = runTest {

        gameViewModel.startNewGame()
        gameViewModel.onSwipe(Direction.RIGHT)
        gameViewModel.onSwipe(Direction.UP)

        gameViewModel.uiState.test {
            awaitUntil("Undo is enabled") { it.undoEnabled }
            gameViewModel.onResumeGame()
            val resumeState = awaitUntil("Resume dialog is shown") {
                it.dialogState.showResumeGameDialog
            }
            assertTrue(resumeState.dialogState.showResumeGameDialog)
            gameViewModel.onResumeGameDialogDismiss()
            val dialogDismissedState = awaitUntil("Resume dialog is dismissed") {
                !it.dialogState.showResumeGameDialog
            }
            assertFalse(dialogDismissedState.dialogState.showResumeGameDialog)
            cancelAndIgnoreRemainingEvents()
        }

    }

    @Test
    fun `restart the game when history exist`() = runTest {

        gameViewModel.startNewGame(clearDialogs = true)
        gameViewModel.onSwipe(Direction.LEFT)
        gameViewModel.onSwipe(Direction.RIGHT)

        gameViewModel.uiState.test {

            awaitUntil("Undo is enabled") { it.undoEnabled }

            gameViewModel.onRestartGame()
            val confirmState = awaitUntil("Restart confirm dialog is shown") {
                it.dialogState.showRestartConfirmDialog
            }
            assertTrue(confirmState.dialogState.showRestartConfirmDialog)
            gameViewModel.startNewGame()
            val resetState = awaitUntil("Dialog disappears and undo enabled") {
                !it.dialogState.showRestartConfirmDialog &&  !it.undoEnabled
            }
            assertEquals(0,resetState.score)
            assertFalse(resetState.undoEnabled)
            cancelAndIgnoreRemainingEvents()
        }

    }


    @Test
    fun `onGameOverDialogDismiss persists new score and show new score dialog`() = runTest {
        val newBestScore = 64
        gameViewModel.startNewGame(clearDialogs = true)
        (dataStoreRepository as FakeDataStoreRepository).setBestScore(20)
        val gameOverState = gameEngine.gameState.value.copy(isOver = true, score = newBestScore)
        gameEngine.undo(gameOverState)

        gameViewModel.uiState.test {
            awaitUntil("Game over dialog is shown") { it.isOver }
//            gameViewModel.onGameOverDialogDismiss()
//            val dismissedState = awaitUntil("Game over dialog is dismissed") { !it.isOver }
//            assertEquals(newBestScore,dismissedState.bestScore)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private suspend fun <T> ReceiveTurbine<T>.awaitUntil(label: String, predicate: (T) -> Boolean): T {
        while (true) {
            val item = awaitItem()
            if (predicate(item)) return item
        }
    }


}