package com.codebythura.baselineprofile

import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiAutomatorTestScope


fun UiAutomatorTestScope.swipeGameContent() {
    onElementOrNull { viewIdResourceName == "game_content" }?.let { board ->
        board.fling(Direction.LEFT)
        board.fling(Direction.RIGHT)
        board.fling(Direction.UP)
        board.fling(Direction.DOWN)
    } ?: error("game_content not found")
}

fun UiAutomatorTestScope.dismissResumeDialogIfShown() {
    onElementOrNull(timeoutMs = 2_000L) {
        viewIdResourceName == "dialog_resume_game_button"
    }?.let { button ->
        button.click()
        waitForStableInActiveWindow()
    }
}