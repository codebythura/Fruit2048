package com.codebythura.fruit2048.grid_matrix

import com.codebythura.fruit2048.data.GRID_SIZE
import com.codebythura.fruit2048.data.GridData
import com.codebythura.fruit2048.data.INVALID_LIST_MESSAGE
import com.codebythura.fruit2048.data.padToGridSize
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class PadToGridSizeTest {

    @Test
    fun `padToGriSize with over sized list`() {
        val row = List<GridData?>(GRID_SIZE + 1) { null }
        val exception = assertThrows(IllegalStateException::class.java) {
            row.padToGridSize()
        }
        assertEquals(INVALID_LIST_MESSAGE,exception.message)
    }

    @Test
    fun `padToGridSize with empty list`() {
        val row = emptyList<GridData?>()
        val newRow = row.padToGridSize()
        assertEquals(GRID_SIZE,newRow.size)
        assert(newRow.all { value -> value == null })
    }

    @Test
    fun `padToGridSize with complete row`() {
        val row = List<GridData?>(GRID_SIZE) { null }
        val newRow = row.padToGridSize()
        assertEquals(GRID_SIZE,newRow.size)
        assert(newRow.all { value -> value == null })
    }

    @Test
    fun `padToGridSize with incomplete row`() {
        val row = listOf<GridData?>(null,null)
        val newRow = row.padToGridSize()
        assertEquals(GRID_SIZE,newRow.size)
        assert(newRow.all { value -> value == null })
    }

}