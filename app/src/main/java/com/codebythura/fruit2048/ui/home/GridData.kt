package com.codebythura.fruit2048.ui.home

import com.codebythura.fruit2048.ui.home.components.TileData
import java.util.UUID


data class GridData(
    val id : String = UUID.randomUUID().toString(),
    var value : Int,
) {
    fun intoTile(row: Int,col: Int) = TileData(
        id = id,
        value = value,
        row = row,
        col = col,
    )

    override fun toString(): String {
        return "$value"
    }
}