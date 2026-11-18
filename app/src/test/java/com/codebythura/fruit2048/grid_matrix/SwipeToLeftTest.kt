package com.codebythura.fruit2048.grid_matrix

import com.codebythura.fruit2048.data.GRID_SIZE
import com.codebythura.fruit2048.data.GridData
import com.codebythura.fruit2048.data.GridMatrix
import com.codebythura.fruit2048.data.INVALID_GRID_ERROR_MESSAGE
import com.codebythura.fruit2048.data.swipeToLeft
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test


class SwipeToLeftTest {

    @Test
    fun `swipeDown zero sized grid`() {

        val initialGird = listOf<List<GridData?>>()
        val exception = assertThrows(IllegalStateException::class.java) {
            initialGird.swipeToLeft()
        }
        assertEquals(INVALID_GRID_ERROR_MESSAGE,exception.message)

    }

    @Test
    fun `swipeToLeft invalid grid matrix`() {

        val initialGrid = listOf(
            List(GRID_SIZE + 1) { null },
            List(GRID_SIZE + 1) { null },
            List(GRID_SIZE ) { null },
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
        )

        val exception = assertThrows(IllegalStateException::class.java) {
            initialGrid.swipeToLeft()
        }
        assertEquals(INVALID_GRID_ERROR_MESSAGE,exception.message)

    }

    @Test
    fun `swipeToLeft incomplete rows and pad to GridSize`() {
        val initialGrid : GridMatrix = listOf(
            listOf(null, GridData(value = 2),null),
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
        )

        val (newGrid,score) = initialGrid.swipeToLeft()
        assertEquals(GRID_SIZE,newGrid[0].size)
        assertEquals(2,newGrid[0][0]?.value)
        assertNull(newGrid[0][1])
    }

    @Test
    fun `swipeToLeft merge adjacent identical tiles and accumulate score`() {

        val initialGrid : GridMatrix = listOf(
            listOf(null,GridData(value = 2),null, GridData(value = 2)),
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
        )

        val (newGrid,score) = initialGrid.swipeToLeft()
        assertEquals(4,score)
        assertEquals(4,newGrid[0][0]?.value)
        assertNull(newGrid[0][1])
        assertNull(newGrid[0][3])
    }

    @Test
    fun `swipeToLeft shifts scattered tiles to the left correctly`() {

        val initialGrid: GridMatrix = listOf(
            listOf(null, GridData(value = 2), null, GridData(value = 4)),
            listOf(null, null, null, null),
            listOf(null, null, null, null),
            listOf(null, null, null, null)
        )

        val (newGrid,score) = initialGrid.swipeToLeft()

        assertEquals("Score should be 0 since no merges occurred", 0, score)
        assertEquals(2, newGrid[0][0]?.value)
        assertEquals(4, newGrid[0][1]?.value)
        assertNull(newGrid[0][2])
        assertNull(newGrid[0][3])
    }

}