package com.codebythura.fruit2048.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.codebythura.fruit2048.ui.game.GameScreen


@Composable
fun Fruit2048App(modifier: Modifier = Modifier) {
    // make the app responsive ui here
    GameScreen(modifier = modifier.semantics { testTagsAsResourceId = true })
}