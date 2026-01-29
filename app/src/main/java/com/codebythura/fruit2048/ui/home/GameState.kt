package com.codebythura.fruit2048.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import kotlin.random.Random

//private const val GRID_SIZE = 4
//private val emptyGrid: Array<Array<GridData?>> = Array(GRID_SIZE) { Array(GRID_SIZE) { null } }
//
//
//private fun printRow(array : Array<GridData?>) {
//    for(value in array) {
//        if(value == null) {
//            print("n ,")
//        }
//        else {
//            print("$value ,")
//        }
//    }
//    println()
//}
//
//private fun printGrid(grid : Array<Array<GridData?>>) {
//    for(row in grid) {
//        printRow(row)
//    }
//}
//
//
//class GameState(data : Array<Array<GridData?>>? = null) {
//
//    var gridData by mutableStateOf(data ?: emptyGrid)
//    private set
//
//    init {
//        gridData = data ?: emptyGrid
//        printGrid(gridData)
//        addRandomData(count = if(data != null) 1 else 2)
//    }
//
//    fun onSwipe(direction: Direction) {
//        when(direction) {
//            Direction.Left -> swipeToLeft()
//            Direction.Right -> swipeToRight()
//            Direction.Up -> swipeUp()
//            Direction.Down -> swipeDown()
//        }
////        printGrid(gridData)
//    }
//
//    fun addRandomData(count : Int = 1) {
//        if (count <= 0) return
//        val emptyCells = gridData.flatMapIndexed { row, list ->
//            list.mapIndexedNotNull { col, data ->
//                if (data == null) row to col else null
//            }
//        }.shuffled()
//        if (emptyCells.isEmpty()) return
//        for (i in 0 until count) {
//            val cell = emptyCells[i]
//            val value = if (Random.nextFloat() < 0.9f) 2 else 4
//            gridData[cell.first][cell.second] = GridData(value = value)
//        }
//    }

//    fun isGameOver() : Boolean {
//        if(hasEmptyCells()) return false
//        if(canMerge()) return false
//        return true
//    }
//
//    private fun hasEmptyCells(): Boolean {
//        return gridData.any { row -> row.any { it == null } }
//    }
//
//    private fun canMerge(): Boolean {
//        for (r in 0 until GRID_SIZE) {
//            for (c in 0 until GRID_SIZE) {
//                val current = gridData[r][c]
//                // Check right neighbor
//                if (c + 1 < GRID_SIZE && current == gridData[r][c + 1]) return true
//                // Check bottom neighbor
//                if (r + 1 < GRID_SIZE && current == gridData[r + 1][c]) return true
//            }
//        }
//        return false
//    }
//
//    private fun swipeDown() {
//        rotate90Clockwise()
//        swipeToLeft()
//        rotate90AntiClockwise()
//    }
//
//    private fun swipeUp()  {
//        rotate90AntiClockwise()
//        swipeToLeft()
//        rotate90Clockwise()
//    }
//
//    private fun swipeToRight() {
//        gridData = gridData.map { row -> row.reversedArray() }.toTypedArray()
//        swipeToLeft()
//        gridData = gridData.map { row -> row.reversedArray() }.toTypedArray()
//    }
//
//    private fun swipeToLeft() {
//        gridData = gridData.map { row ->
//            val merged = mergeToLeft(row)
//            padToGridSize(merged)
//        }.toTypedArray()
//    }
//
//    private fun rotate90AntiClockwise() {
//        gridData = Array(GRID_SIZE) { row ->
//            Array(GRID_SIZE) { col ->
//                gridData[col][GRID_SIZE - 1 - row]
//            }
//        }
//    }
//
//    private fun rotate90Clockwise() {
//        gridData = Array(GRID_SIZE) { row ->
//            Array(GRID_SIZE) { col ->
//                gridData[GRID_SIZE - 1 - col][row]
//            }
//        }
//    }
//
//}
//
//private fun padToGridSize(array: Array<GridData?>): Array<GridData?> {
//    val missing = (GRID_SIZE - array.size).coerceAtLeast(0)
//    return array + Array(missing) { null }
//}
//
//private fun mergeToLeft(array: Array<GridData?>): Array<GridData?> {
//    val notNulls = array.filterNotNull()
//    val merged = mutableListOf<GridData>()
//    var i = 0
//    while (i < notNulls.size) {
//        if (i + 1 < notNulls.size && notNulls[i].value == notNulls[i + 1].value) {
//            val data = notNulls[i]
//            merged.add(data.copy(value = data.value * 2))
//            i += 2
//        } else {
//            merged.add(notNulls[i])
//            i++
//        }
//    }
//    return merged.toTypedArray()
//}