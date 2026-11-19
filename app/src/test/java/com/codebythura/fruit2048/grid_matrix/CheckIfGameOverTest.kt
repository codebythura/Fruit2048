package com.codebythura.fruit2048.grid_matrix

import com.codebythura.fruit2048.data.EMPTY_GRID
import com.codebythura.fruit2048.data.GridData
import com.codebythura.fruit2048.data.INVALID_GRID_MESSAGE
import com.codebythura.fruit2048.data.checkIfGameOver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class CheckIfGameOverTest {

    @Test
    fun `checkIfGameOver invalid empty grid throws illegal state exception`() {
        val initialGrid = listOf<List<GridData?>>()
        val exception = assertThrows(IllegalStateException::class.java) {
            initialGrid.checkIfGameOver()
        }
        assertEquals(INVALID_GRID_MESSAGE,exception.message)
    }

    @Test
    fun `checkIfGameOver invalid grid throws illegal state exception`() {

        val initialGrid = listOf(
            listOf(GridData(value = 2),GridData(value = 2),GridData(value = 2),GridData(value = 2),GridData(value = 2)),
            listOf(GridData(value = 2),GridData(value = 2)),
            List(2) { null },
            listOf()
        )

        val exception = assertThrows(IllegalStateException::class.java) {
            initialGrid.checkIfGameOver()
        }
        assertEquals(INVALID_GRID_MESSAGE,exception.message)

    }

    @Test
    fun `checkIfGameOver sample grid`() {

        val initialGrid = listOf(
            listOf(GridData(value = 2), GridData(value = 16),GridData(value = 2),GridData(value = 16)),
            listOf(GridData(value = 8), GridData(value = 32),GridData(value = 4),GridData(value = 32)),
            listOf(GridData(value = 4), GridData(value = 128),GridData(value = 2),GridData(value = 256)),
            listOf(GridData(value = 16), GridData(value = 2),GridData(value = 4),GridData(value = 2)),
        )

        val isGameOver = initialGrid.checkIfGameOver()
        assertTrue(isGameOver)

    }

    @Test
    fun `checkIfGameOver empty grid`() {

        val initialGrid = EMPTY_GRID
        val isGameOver = initialGrid.checkIfGameOver()

        assertFalse(isGameOver)

    }

}