package com.codebythura.fruit2048.grid_matrix

import com.codebythura.fruit2048.data.GRID_SIZE
import com.codebythura.fruit2048.data.GridData
import com.codebythura.fruit2048.data.GridMatrix
import com.codebythura.fruit2048.data.INVALID_GRID_ERROR_MESSAGE
import com.codebythura.fruit2048.data.swipeDown
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class SwipeDownTest {


    @Test
    fun `swipeDown zero sized grid matrix`() {

        val initialGrid = listOf<List<GridData?>>()
        val exception = assertThrows(IllegalStateException::class.java) {
            initialGrid.swipeDown()
        }
        assertEquals(INVALID_GRID_ERROR_MESSAGE,exception.message)
    }

    @Test
    fun `swipeDown over size grid matrix`() {

        val initialGrid = listOf(
            List(GRID_SIZE + 1) { null },
            List(GRID_SIZE + 1) { null },
            List(GRID_SIZE ) { null },
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
        )

        val exception = assertThrows(IllegalStateException::class.java) { initialGrid.swipeDown() }
        assertEquals(INVALID_GRID_ERROR_MESSAGE,exception.message)

    }


    @Test
    fun `swipeDown incomplete row and pad to GRID_SIZE`() {

        val initialGrid = listOf(
            listOf(null),
            listOf(null),
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
        )

        val (newGrid,score) = initialGrid.swipeDown()

        assertEquals(0, score)
        assert(newGrid.all { it.size == GRID_SIZE })

    }


    @Test
    fun `swipeDown two different values and check score`() {

        val initialGrid : GridMatrix = listOf(
            listOf(null, GridData(value = 2),null,GridData(value = 2)),
            listOf(null,GridData(value = 4),null,null),
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
        )

        val (newGrid,score) = initialGrid.swipeDown()

        assertEquals(0,score)
        assertNull(newGrid[0][1])
        assertNull(newGrid[1][1])


    }

    @Test
    fun `swipeDown two identical values and check score`() {

        val initialGrid : GridMatrix = listOf(
            listOf(null, GridData(value = 2),null,GridData(value = 2)),
            listOf(GridData(value = 4),null,null,GridData(value = 2)),
            List(GRID_SIZE) { null },
            List(GRID_SIZE) { null },
        )

        val (newGrid,score) = initialGrid.swipeDown()

        assertEquals(4,score)
        assertEquals(4,newGrid[3][3]?.value)
        assertEquals(4,newGrid[3][0]?.value)
        assertNull(newGrid[0][1])
        assertNull(newGrid[0][3])

    }

}