package com.codebythura.fruit2048

import com.codebythura.fruit2048.data.Direction
import com.codebythura.fruit2048.data.EMPTY_GRID
import com.codebythura.fruit2048.data.GRID_SIZE
import com.codebythura.fruit2048.data.GameEngine
import com.codebythura.fruit2048.data.GameEngineImpl
import com.codebythura.fruit2048.data.GameEngineState
import com.codebythura.fruit2048.data.GridData
import com.codebythura.fruit2048.data.GridMatrix
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GameEngineTest {

    private lateinit var gameEngine: GameEngine

    @Before
    fun setup() {
        gameEngine = GameEngineImpl()
    }

    @Test
    fun `onSwipe set isOver true when the tile has no more empty cell`() {

        val initialGrid : GridMatrix = listOf(
            listOf(GridData(value = 2), GridData(value = 4),GridData(value = 2),GridData(value = 4)),
            listOf(GridData(value = 4), GridData(value = 2),GridData(value = 4),GridData(value = 2)),
            listOf(GridData(value = 2), GridData(value = 4),GridData(value = 2),GridData(value = 128)),
            listOf(GridData(value = 4), GridData(value = 2),GridData(value = 256),null),
        )

        val initialState = GameEngineState(
            gridData = initialGrid,
            score = 12,
            isOver = false
        )

        gameEngine.startGame(initialState)
        gameEngine.onSwipe(Direction.LEFT)

        val updatedState = gameEngine.gameState.value
        val nullCount = updatedState.gridData.flatten().count { it == null }

        assertEquals(0,nullCount)
        assertTrue(updatedState.isOver)

    }


    @Test
    fun `onSwipeLeft merge tiles and check the score`() {

        val initialGrid : GridMatrix = listOf(
            listOf(GridData(value = 2), GridData(value = 2),GridData(value = 4)),
            listOf(null, GridData(value = 2),null,null),
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
        )

        val initialState = GameEngineState(
            gridData = initialGrid,
            score = 12,
            isOver = false
        )

        gameEngine.startGame(initialState)
        gameEngine.onSwipe(Direction.LEFT)

        val updatedState = gameEngine.gameState.value

        assertEquals(16,updatedState.score)
        assertFalse(updatedState.isOver)

        val notNullsCount = updatedState.gridData.flatten().count { it != null }
        assertEquals(4,notNullsCount)

    }


    @Test
    fun `start game engine with a default state and restore to the state`() {

        val initialGrid : GridMatrix = listOf(
            listOf(null, GridData(value = 2),GridData(value = 4),null),
            listOf(null, GridData(value = 2),null,null),
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
        )

        val previousState = GameEngineState(
            gridData = initialGrid,
            score = 42,
            isOver = false
        )

        gameEngine.undo(previousState)
        assertEquals(previousState,gameEngine.gameState.value)

    }

    @Test
    fun `state game engine with an explicit state`() {

        val initialGrid : GridMatrix = listOf(
            listOf(null, GridData(value = 2),GridData(value = 4),null),
            listOf(null, GridData(value = 2),null,null),
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
        )

        val initialState = GameEngineState(
            gridData = initialGrid,
            score = 42,
            isOver = false
        )
        gameEngine.startGame(initialState)

        assertEquals(initialState,gameEngine.gameState.value)
        assertEquals(initialGrid,gameEngine.gameState.value.gridData)
        assertEquals(42,gameEngine.gameState.value.score)
        assertFalse(gameEngine.gameState.value.isOver)

    }

    @Test
    fun `start game engine with default state`() {

        gameEngine.startGame()
        val state = gameEngine.gameState.value
        val spawnedTiles = state.gridData.flatten().filterNotNull()

        assertEquals(2,spawnedTiles.size)
        assertTrue(spawnedTiles.all { it.value == 2 || it.value == 4 })

        assertEquals(0,state.score)
        assertFalse(state.isOver)


    }

    @Test
    fun `initialize game engine with initial game engine state`() {

        val state = gameEngine.gameState.value

        assertEquals(EMPTY_GRID,state.gridData)
        assertEquals(0,state.score)
        assertFalse(state.isOver)


    }

}