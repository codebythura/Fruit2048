package com.codebythura.fruit2048.data

import androidx.annotation.VisibleForTesting
import kotlin.random.Random

const val GRID_SIZE = 4
typealias GridMatrix = List<List<GridData?>>
val EMPTY_GRID: GridMatrix = List(GRID_SIZE) { List(GRID_SIZE) { null } }

const val INVALID_GRID_MESSAGE = "Grid size must be between 0 and $GRID_SIZE."
const val INVALID_LIST_MESSAGE = "List size must be between 0 and $GRID_SIZE."
const val INVALID_RANDOM_COUNT_MESSAGE = "Count must be greater than 0."

fun GridMatrix.checkIfGameOver(): Boolean {
    if(this.size != GRID_SIZE || this.any { it.size !in 0..GRID_SIZE }) throw IllegalStateException(INVALID_GRID_MESSAGE)
    for (r in 0 until GRID_SIZE) {
        for (c in 0 until GRID_SIZE) {
            val currentValue = this[r][c] ?: return false
            if (c + 1 < GRID_SIZE) {
                val rightValue = this[r][c + 1]
                if (rightValue == null || rightValue.value == currentValue.value) return false
            }
            if (r + 1 < GRID_SIZE) {
                val downValue = this[r + 1][c]
                if (downValue == null || downValue.value == currentValue.value) return false
            }
        }
    }
    return true
}

fun GridMatrix.addRandomData(count : Int = 1) : GridMatrix {
    if (count <= 0) throw IllegalArgumentException(INVALID_RANDOM_COUNT_MESSAGE)
    val emptyCells = this.flatMapIndexed { row, list ->
        list.mapIndexedNotNull { col, data ->
            if (data == null) row to col else null
        }
    }.shuffled()
    if (emptyCells.isEmpty()) return this

    val actualCount = minOf(emptyCells.size, count)
    val newGrid = this.map { it.toMutableList() }.toMutableList()

    for (i in 0 until actualCount) {
        val cell = emptyCells[i]
        val value = if (Random.nextFloat() < 0.9f) 2 else 4
        newGrid[cell.first][cell.second] = GridData(value = value)
    }
    return newGrid
}

fun GridMatrix.swipeUp(): Pair<GridMatrix, Int> {
    if(this.size != GRID_SIZE || this.any { it.size !in 0..GRID_SIZE }) throw IllegalStateException(INVALID_GRID_MESSAGE)
    val rotated90AntiClockwise = this.map { it.padToGridSize() }.rotate90AntiClockwise()
    val (swipedToLeft, score) = rotated90AntiClockwise.swipeToLeft()
    val rotated90Clockwise = swipedToLeft.rotate90Clockwise()
    return Pair(rotated90Clockwise, score)
}

fun GridMatrix.swipeDown(): Pair<GridMatrix, Int> {
    if(this.size != GRID_SIZE || this.any { it.size !in 0..GRID_SIZE }) throw IllegalStateException(INVALID_GRID_MESSAGE)
    val rotated90Clockwise = this.map { it.padToGridSize() }.rotate90Clockwise()
    val (swipedToLeft, score) = rotated90Clockwise.swipeToLeft()
    val rotated90AntiClockwise = swipedToLeft.rotate90AntiClockwise()
    return Pair(rotated90AntiClockwise, score)
}

fun GridMatrix.swipeToLeft(): Pair<GridMatrix, Int> {
    if(this.size != GRID_SIZE || this.any { it.size !in 0..GRID_SIZE }) throw IllegalStateException(INVALID_GRID_MESSAGE)
    var totalScore = 0
    val swipedToLeft = this.map { row ->
        val (merged, score) = row.mergeToLeft()
        totalScore += score
        merged.padToGridSize()
    }
    return Pair(swipedToLeft, totalScore)
}

fun GridMatrix.swipeToRight(): Pair<GridMatrix, Int> {
    if(this.size != GRID_SIZE || this.any { it.size !in 0..GRID_SIZE }) throw IllegalStateException(INVALID_GRID_MESSAGE)
    val reversed = this.map { row -> row.reversed() }
    val (swipedToLeft, score) = reversed.swipeToLeft()
    val swipedToRight = swipedToLeft.map { row -> row.reversed() }
    return Pair(swipedToRight, score)
}

fun GridMatrix.rotate90Clockwise(): GridMatrix {
    return List(GRID_SIZE) { row ->
        List(GRID_SIZE) { col ->
            this[GRID_SIZE - 1 - col][row]
        }
    }
}

fun GridMatrix.rotate90AntiClockwise() : GridMatrix {
    return List(GRID_SIZE) { row ->
        List(GRID_SIZE) { col ->
            this[col][GRID_SIZE - 1 - row]
        }
    }
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun List<GridData?>.padToGridSize(): List<GridData?> {
    if(this.size > GRID_SIZE) throw IllegalStateException(INVALID_LIST_MESSAGE)
    val missing = (GRID_SIZE - this.size).coerceAtLeast(0)
    return this + List(missing) { null }
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun List<GridData?>.mergeToLeft(): Pair<List<GridData?>, Int> {
    if(this.size !in 0..GRID_SIZE) throw IllegalStateException(INVALID_LIST_MESSAGE)
    val notNulls = this.filterNotNull()
    val merged = mutableListOf<GridData>()
    var mergedScore = 0
    var i = 0
    while (i < notNulls.size) {
        if (i + 1 < notNulls.size && notNulls[i].value == notNulls[i + 1].value) {
            val data = notNulls[i]
            merged.add(data.copy(value = data.value * 2))
            i += 2
            mergedScore += data.value * 2
        } else {
            merged.add(notNulls[i])
            i++
        }
    }
    return Pair(merged, mergedScore)
}


