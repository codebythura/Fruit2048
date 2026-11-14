package com.codebythura.fruit2048.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.codebythura.fruit2048.ui.game.GameScreen


@Composable
fun Fruit2048App(modifier: Modifier = Modifier) {
    GameScreen(modifier = modifier)
}