package com.codebythura.fruit2048.data

data class TileData(
    val id: String,
    val value: Int,
    val row: Int,
    val col: Int,
    val shouldAnimate : Boolean,
)
