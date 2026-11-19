package com.codebythura.fruit2048.grid_matrix

import com.codebythura.fruit2048.data.GRID_SIZE
import com.codebythura.fruit2048.data.GridData
import com.codebythura.fruit2048.data.GridMatrix
import com.codebythura.fruit2048.data.INVALID_GRID_MESSAGE
import com.codebythura.fruit2048.data.swipeUp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class SwipeUpTest {

    @Test
    fun `swipeUp zero sized grid matrix`() {

        val initialGrid = listOf<List<GridData?>>()
        val exception = assertThrows(IllegalStateException::class.java) {
            initialGrid.swipeUp()
        }
        assertEquals(INVALID_GRID_MESSAGE,exception.message)
    }


    @Test
    fun `swipeUp invalid grid and check exception message`() {

        val gridMatrix : GridMatrix = listOf(
            List(GRID_SIZE + 1) { null },
            List(GRID_SIZE + 1) { null },
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
        )

        val exception = assertThrows(IllegalStateException::class.java) { gridMatrix.swipeUp() }
        assertEquals(INVALID_GRID_MESSAGE,exception.message)

    }

    @Test
    fun `swipeUp incomplete row and pad into GRID_SIZE`() {

        val initialGrid : GridMatrix = listOf(
            listOf(null, GridData(value = 2),GridData(value = 2),null),
            listOf(GridData(value = 4),null),
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
        )

        val (newGrid,score) = initialGrid.swipeUp()
        assertEquals(GRID_SIZE,newGrid[1].size)
        assertEquals(0,score)
        assertEquals(4,newGrid[0][0]?.value)

    }

    @Test
    fun `swipeUp two scattered values and check score`() {

        val initialGrid : GridMatrix = listOf(
            listOf(null, GridData(value = 2),GridData(value = 2),null),
            List(GRID_SIZE) { null },
            listOf(null, GridData(value = 4),null,null),
            List(GRID_SIZE) { null },
        )

        val (newGrid,score) = initialGrid.swipeUp()
        assertEquals(0,score)
        assertEquals(2,newGrid[0][1]?.value)
        assertEquals(4,newGrid[1][1]?.value)
        assertNull(newGrid[2][1])

    }

    @Test
    fun `swipeUp two identical adjacent values and check score`() {

        val initialGrid : GridMatrix = listOf(
            listOf(null, GridData(value = 2),GridData(value = 2),null),
            listOf(null, GridData(value = 2),null,null),
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
        )

        val (newGrid,score) = initialGrid.swipeUp()

        assertEquals(4,newGrid[0][1]?.value)
        assertNull(newGrid[1][1])
        assertEquals(4,score)

    }

}