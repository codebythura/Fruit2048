package com.codebythura.macrobenchmark

import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiAutomatorTestScope
import androidx.test.uiautomator.uiAutomator
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SwipeBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun swipe() {
        benchmarkRule.measureRepeated(
            packageName = "com.codebythura.fruit2048",
            metrics = listOf(FrameTimingMetric()),
            iterations = 5,
            startupMode = StartupMode.COLD,
            setupBlock = { uiAutomator { pressHome() } }
        ) {
            uiAutomator {
                startApp("com.codebythura.fruit2048")
                dismissResumeDialogIfShown()
                swipeGameContent()
            }
        }
    }
}

private fun UiAutomatorTestScope.swipeGameContent() {
    onElementOrNull { viewIdResourceName == "game_content" }?.let { board ->
        board.fling(Direction.LEFT)
        board.fling(Direction.RIGHT)
        board.fling(Direction.UP)
        board.fling(Direction.DOWN)
    } ?: error("game_content not found")
}

private fun UiAutomatorTestScope.dismissResumeDialogIfShown() {
    onElementOrNull(timeoutMs = 2_000L) {
        viewIdResourceName == "dialog_resume_game_button"
    }?.let { button ->
        button.click()
        waitForStableInActiveWindow()
    }
}