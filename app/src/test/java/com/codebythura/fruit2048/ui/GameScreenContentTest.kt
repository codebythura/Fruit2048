package com.codebythura.fruit2048.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.codebythura.fruit2048.R
import com.codebythura.fruit2048.data.Direction
import com.codebythura.fruit2048.ui.game.GameDialogState
import com.codebythura.fruit2048.ui.game.GameScreenContent
import com.codebythura.fruit2048.ui.game.GameUIState
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class GameScreenContentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    val activity : ComponentActivity by lazy { composeTestRule.activity }

    @Test
    fun `invoke game over dialog callbacks`() {

        var tryAgainCall = 0
        val gameUIState = GameUIState(isOver = true)

        composeTestRule.setContent {
            GameScreenContent(
                state = gameUIState,
                startNewGame = {},
                onUndo = {},
                onSwipe = {},
                onRestartGame = {},
                onGameOverDialogDismiss = { tryAgainCall++ },
                onNewScoreDialogDismiss = {},
                onRestartGameDialogDismiss = {},
                onResumeGameDialogDismiss = {},
                modifier = Modifier.fillMaxSize(),
            )
        }

        composeTestRule.waitUntil(timeoutMillis = 500) {
            composeTestRule
                .onAllNodesWithTag(activity.getString(R.string.game_over_dialog))
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule
            .onNodeWithTag(activity.getString(R.string.game_over_dialog))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(activity.getString(R.string.dialog_try_again_button))
            .assertIsDisplayed()
            .performClick()

        assertEquals(1,tryAgainCall)

    }

    @Test
    fun `invoke new score dialog callbacks`() {

        var dismissCall = 0
        val gameUIState = GameUIState(dialogState = GameDialogState(showNewScoreDialog = true))

        composeTestRule.setContent {
            GameScreenContent(
                state = gameUIState,
                startNewGame = {},
                onUndo = {},
                onSwipe = {},
                onRestartGame = {},
                onGameOverDialogDismiss = {},
                onNewScoreDialogDismiss = { dismissCall++ },
                onRestartGameDialogDismiss = {},
                onResumeGameDialogDismiss = {},
                modifier = Modifier.fillMaxSize(),
            )
        }

        composeTestRule
            .onNodeWithTag(activity.getString(R.string.new_best_score_dialog))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(activity.getString(R.string.dialog_close_button))
            .assertIsDisplayed()
            .performClick()

        assertEquals(1,dismissCall)

    }

    @Test
    fun `invoke resume game dialog callbacks`() {

        var resumeCalls = 0
        var newGameCalls = 0
        val stateFlow = MutableStateFlow(GameUIState(dialogState = GameDialogState(showResumeGameDialog = true)))

        composeTestRule.setContent {
            val state by stateFlow.collectAsStateWithLifecycle()
            GameScreenContent(
                state = state,
                startNewGame = { newGameCalls++; stateFlow.value = GameUIState() },
                onUndo = {},
                onSwipe = {},
                onRestartGame = {},
                onGameOverDialogDismiss = {},
                onNewScoreDialogDismiss = {},
                onRestartGameDialogDismiss = {},
                onResumeGameDialogDismiss = { resumeCalls++; stateFlow.value = GameUIState() },
                modifier = Modifier.fillMaxSize(),
            )
        }


        // Resume game click
        composeTestRule
            .onNodeWithTag(activity.getString(R.string.game_resume_dialog))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(activity.getString(R.string.resume_game_button))
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithTag(activity.getString(R.string.game_resume_dialog))
            .assertIsNotDisplayed()

        assertEquals(1,resumeCalls)
        assertEquals(0,newGameCalls)

        // New game click

        stateFlow.value = GameUIState(dialogState = GameDialogState(showResumeGameDialog = true))

        composeTestRule
            .onNodeWithTag(activity.getString(R.string.game_resume_dialog))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(activity.getString(R.string.dialog_new_game_button))
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithTag(activity.getString(R.string.game_resume_dialog))
            .assertIsNotDisplayed()

        assertEquals(1,resumeCalls)
        assertEquals(1,newGameCalls)

    }

    @Test
    fun `invoke restart confirm dialog callbacks`() {

        var cancelCalls = 0
        var confirmCalls = 0
        val stateFlow = MutableStateFlow(GameUIState(dialogState = GameDialogState(showRestartConfirmDialog = true)))

        composeTestRule.setContent {
            val state by stateFlow.collectAsStateWithLifecycle()
            GameScreenContent(
                state = state,
                startNewGame = { confirmCalls++; stateFlow.value = GameUIState() },
                onUndo = {},
                onSwipe = {},
                onRestartGame = {},
                onGameOverDialogDismiss = {},
                onNewScoreDialogDismiss = {},
                onRestartGameDialogDismiss = { cancelCalls++; stateFlow.value = GameUIState() },
                onResumeGameDialogDismiss = {},
                modifier = Modifier.fillMaxSize(),
            )
        }

        // Confirm Button Clicked
        composeTestRule
            .onNodeWithTag(activity.getString(R.string.restart_confirm_dialog))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(activity.getString(R.string.confirm_button))
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithTag(activity.getString(R.string.restart_confirm_dialog))
            .assertIsNotDisplayed()

        assertEquals(1,confirmCalls)
        assertEquals(0,cancelCalls)

        // Cancel Button Clicked
        stateFlow.value = GameUIState(dialogState = GameDialogState(showRestartConfirmDialog = true))

        composeTestRule
            .onNodeWithTag(activity.getString(R.string.restart_confirm_dialog))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(activity.getString(R.string.cancel_button))
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithTag(activity.getString(R.string.restart_confirm_dialog))
            .assertIsNotDisplayed()

        assertEquals(1,confirmCalls)
        assertEquals(1,cancelCalls)

    }

    @Test
    fun `invokes callbacks for undo, swipe`() {

        var undoCalls = 0
        var lastSwipedDirection : Direction? = null
        val initialState = GameUIState(undoEnabled = true)

        composeTestRule.setContent {
            GameScreenContent(
                state = initialState,
                startNewGame = {},
                onUndo = { undoCalls++ },
                onSwipe = { direction -> lastSwipedDirection = direction },
                onRestartGame = {},
                onGameOverDialogDismiss = {},
                onNewScoreDialogDismiss = {},
                onRestartGameDialogDismiss = {},
                onResumeGameDialogDismiss = {},
                modifier = Modifier.fillMaxSize(),
            )
        }

        composeTestRule
            .onNodeWithTag(activity.getString(R.string.undo_btn))
            .assertIsEnabled()
            .performClick()

        assertEquals(1,undoCalls)

        composeTestRule
            .onNodeWithTag(activity.getString(R.string.game_content))
            .performTouchInput { swipeLeft() }

        assertEquals(Direction.LEFT,lastSwipedDirection)

    }

    @Test
    fun `start the game with a state and check undo`() {

        val stateFlow = MutableStateFlow(
            GameUIState(
                score = 16,
                bestScore = 64,
                isOver = false
            )
        )

        composeTestRule.setContent {

            val uiState by stateFlow.collectAsStateWithLifecycle()

            GameScreenContent(
                state = uiState,
                startNewGame = {},
                onUndo = {},
                onSwipe = {},
                onRestartGame = {},
                onGameOverDialogDismiss = {},
                onNewScoreDialogDismiss = {},
                onRestartGameDialogDismiss = {},
                onResumeGameDialogDismiss = {},
                modifier = Modifier.fillMaxSize(),
            )

        }

        composeTestRule.onNodeWithText("16").assertIsDisplayed()
        composeTestRule.onNodeWithText("64").assertIsDisplayed()
        composeTestRule
            .onNodeWithTag(activity.getString(R.string.undo_btn))
            .assertIsNotEnabled()

        stateFlow.value = GameUIState(score = 32, undoEnabled = true)
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("32").assertIsDisplayed()
        composeTestRule
            .onNodeWithTag(activity.getString(R.string.undo_btn))
            .assertIsEnabled()

    }


}