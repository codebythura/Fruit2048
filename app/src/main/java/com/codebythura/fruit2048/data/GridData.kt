package com.codebythura.fruit2048.data

import java.util.UUID


data class GridData(
    val id : String = UUID.randomUUID().toString(),
    var value : Int,
    val shouldAnimate : Boolean = true,
) {

    fun intoTile(row: Int,col: Int) = TileData(
        id = id,
        value = value,
        row = row,
        col = col,
        shouldAnimate = shouldAnimate,
    )

    override fun toString(): String {
        return "$value"
    }
}