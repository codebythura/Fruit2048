package com.codebythura.fruit2048.grid_matrix

import com.codebythura.fruit2048.data.GRID_SIZE
import com.codebythura.fruit2048.data.GridData
import com.codebythura.fruit2048.data.GridMatrix
import com.codebythura.fruit2048.data.INVALID_GRID_ERROR_MESSAGE
import com.codebythura.fruit2048.data.swipeToRight
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class SwipeToRightTest {

    @Test
    fun `swipeToRight zero sized grid matrix`() {

        val initialGrid = listOf<List<GridData?>>()
        val exception = assertThrows(IllegalStateException::class.java) { initialGrid.swipeToRight() }
        assertEquals(INVALID_GRID_ERROR_MESSAGE,exception.message)

    }

    @Test
    fun `swipeToRight over sized grid matrix`() {

        val initialGrid = listOf(
            List(GRID_SIZE + 1) { null },
            List(GRID_SIZE + 1) { null },
            List(GRID_SIZE + 1) { null },
            List(GRID_SIZE + 1) { null },
            List(GRID_SIZE + 1) { null },
        )

        val exception = assertThrows(IllegalStateException::class.java) {
            initialGrid.swipeToRight()
        }
        assertEquals(INVALID_GRID_ERROR_MESSAGE,exception.message)
    }

    @Test
    fun `swipeToRight incomplete row and pad to GRID_SIZE`() {

        val initialGrid : GridMatrix = listOf(
            listOf(null,null),
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
        )

        val (newGrid,score) = initialGrid.swipeToRight()
        assertEquals(GRID_SIZE,newGrid[0].size)
        assertEquals(0,score)

    }

    @Test
    fun `swipeToRight two scattered values and accumulate score`() {

        val initialGrid : GridMatrix = listOf(
            listOf(null, GridData(value = 2),null,GridData(value = 4)),
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
        )

        val (newGrid,score) = initialGrid.swipeToRight()
        assertEquals(0,score)
        assertEquals(2,newGrid[0][2]?.value)
        assertEquals(4,newGrid[0][3]?.value)
        assertNull(newGrid[0][1])

    }

    @Test
    fun `swipeToRight adjacent identical values and accumulate score`() {

        val initialGrid : GridMatrix = listOf(
            listOf(null, GridData(value = 2),GridData(value = 2),null),
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
        )

        val (newGrid,score) = initialGrid.swipeToRight()

        assertEquals(4,score)
        assertEquals(4,newGrid[0][3]?.value)
        assertNull(newGrid[0][1])
        assertNull(newGrid[0][2])

    }



}