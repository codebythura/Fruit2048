package com.codebythura.fruit2048.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.codebythura.fruit2048.HiltTestActivity
import com.codebythura.fruit2048.R
import com.codebythura.fruit2048.data.GameEngine
import com.codebythura.fruit2048.database.AppDatabase
import com.codebythura.fruit2048.repository.DataStoreRepository
import com.codebythura.fruit2048.ui.game.GameScreen
import com.codebythura.fruit2048.ui.theme.Fruit2048Theme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class GameScreenTest {

    @get:Rule(order = 0)
    val hiltTest = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var dataStoreRepo: DataStoreRepository

    @Inject
    lateinit var gameEngine: GameEngine

    private val activity by lazy { composeRule.activity }

    @Before
    fun setup() {
        hiltTest.inject()
        composeRule.mainClock.autoAdvance = true
    }

    @Test
    fun startGameAndCheckUndoAfterSwipes() {
        setGameScreen()
        undoButton().assertIsDisplayed().assertIsNotEnabled()
        gameContent().performTouchInput { swipeRight() }
        undoButton().assertIsEnabled().performClick()
        composeRule.waitForIdle()
        undoButton().assertIsNotEnabled()
    }

    @Test
    fun cancelRestartConfirmDialogAndUndoEnabled() {

        setGameScreen()
        gameContent().performTouchInput { swipeRight() }
        undoButton().assertIsEnabled()

        newGameButton().assertIsDisplayed().assertIsEnabled().performClick()
        restartConfirmDialog().assertIsDisplayed()
        restartCancelDialogButton().assertIsDisplayed().assertIsEnabled().performClick()
        undoButton().assertIsEnabled()

    }

    @Test
    fun confirmRestartConfirmDialogAndUndoDisabled() {

        setGameScreen()
        gameContent().performTouchInput { swipeRight() }
        undoButton().assertIsEnabled()

        newGameButton().assertIsDisplayed().assertIsEnabled().performClick()
        restartConfirmDialog().assertIsDisplayed()
        restartConfirmDialogButton().assertIsDisplayed().assertIsEnabled().performClick()
        undoButton().assertIsNotEnabled()

    }

    private fun setGameScreen() = composeRule.setContent { Fruit2048Theme { GameScreen() } }

    private fun restartConfirmDialogButton() = composeRule.onNodeWithTag(activity.getString(R.string.confirm_button))

    private fun restartCancelDialogButton() = composeRule.onNodeWithTag(activity.getString(R.string.cancel_button))

    private fun restartConfirmDialog() = composeRule.onNodeWithTag(activity.getString(R.string.restart_confirm_dialog))

    private fun gameContent() = composeRule.onNodeWithTag(activity.getString(R.string.game_content))

    private fun undoButton() = composeRule.onNodeWithTag(activity.getString(R.string.undo_btn))

    private fun newGameButton() = composeRule.onNodeWithTag(activity.getString(R.string.new_game_button))

    @After
    fun teardown() {
        database.close()
    }


}