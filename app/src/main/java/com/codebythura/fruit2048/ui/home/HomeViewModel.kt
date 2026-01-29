package com.codebythura.fruit2048.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.codebythura.fruit2048.ui.home.components.TileData


class HomeViewModel : ViewModel() {

    private var gridData = mutableStateOf(emptyGrid)
    val tileData: List<TileData>
        get() = gridData.value.mapIndexed { row, list ->
            list.mapIndexedNotNull { col, data -> data?.intoTile(row, col) }
        }.flatten()

    init {
        gridData.value = GameLogic.addRandomData(gridData = gridData.value,count = 2)
    }

    fun onSwipe(direction: Direction) {
        gridData.value = when(direction) {
            Direction.Left -> GameLogic.swipeToLeft(gridData.value)
            Direction.Right -> GameLogic.swipeToRight(gridData.value)
            Direction.Up -> GameLogic.swipeUp(gridData.value)
            Direction.Down -> GameLogic.swipeDown(gridData.value)
        }
        gridData.value = GameLogic.addRandomData(gridData.value)
    }

}
