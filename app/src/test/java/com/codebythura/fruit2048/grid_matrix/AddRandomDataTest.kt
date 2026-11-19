package com.codebythura.fruit2048.grid_matrix

import com.codebythura.fruit2048.data.EMPTY_GRID
import com.codebythura.fruit2048.data.GRID_SIZE
import com.codebythura.fruit2048.data.GridData
import com.codebythura.fruit2048.data.GridMatrix
import com.codebythura.fruit2048.data.INVALID_RANDOM_COUNT_MESSAGE
import com.codebythura.fruit2048.data.addRandomData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

private fun GridMatrix.countNulls(): Int =
    sumOf { row -> row.count { it == null } }

class AddRandomDataTest {

    @Test
    fun `addRandomData return same grid when no empty cell exists`() {

        val initialGrid : GridMatrix = List(GRID_SIZE) { List(GRID_SIZE) { GridData(value = 2) } }
        val result = initialGrid.addRandomData()
        assertSame(initialGrid,result)

    }


    @Test
    fun `addRandomData to Grid matrix with 1 empty cell`() {

        val initialGrid : GridMatrix = listOf(
            listOf(GridData(value = 2),GridData(value = 2),GridData(value = 2),GridData(value = 2)),
            listOf(GridData(value = 16),GridData(value = 4),GridData(value = 2),GridData(value = 8)),
            listOf(GridData(value = 2),GridData(value = 2),GridData(value = 2),GridData(value = 2)),
            listOf(GridData(value = 2),GridData(value = 2),GridData(value = 2),null),
        )

        val result = initialGrid.addRandomData()
        assertNotNull(result[3][3])
        assertEquals(0,result.countNulls())
        assertTrue(result[3][3]?.value in listOf(2,4))

    }

    @Test
    fun `addRandomData to EMPTY Grid and check the empty cells`() {

        val count = 2
        val result = EMPTY_GRID.addRandomData(count = count)
        assertEquals(16 - count,result.countNulls())

        val spawnedValues = result.flatten().filterNotNull().map { it.value }
        assertEquals(2,spawnedValues.size)
        assertTrue(spawnedValues.all { it == 2 || it == 4 })

    }

    @Test
    fun `addRandomData to EMPTY Grid with invalid counts throws IllegalArgumentException`() {

        val initialGrid = EMPTY_GRID
        val exception1 = assertThrows(IllegalArgumentException::class.java) {
            initialGrid.addRandomData(count = 0)
        }
        assertEquals(INVALID_RANDOM_COUNT_MESSAGE,exception1.message)

        val exception2 = assertThrows(IllegalArgumentException::class.java) {
            initialGrid.addRandomData(count = -5)
        }
        assertEquals(INVALID_RANDOM_COUNT_MESSAGE,exception2.message)
    }



}