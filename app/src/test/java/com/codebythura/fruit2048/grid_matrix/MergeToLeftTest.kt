package com.codebythura.fruit2048.grid_matrix

import com.codebythura.fruit2048.data.GRID_SIZE
import com.codebythura.fruit2048.data.GridData
import com.codebythura.fruit2048.data.INVALID_LIST_ERROR_MESSAGE
import com.codebythura.fruit2048.data.mergeToLeft
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class MergeToLeftTest {

    @Test
    fun `mergeToLeft a over sized list`() {
        val row = List(GRID_SIZE + 1) { null }
        val exception = assertThrows(IllegalStateException::class.java) { row.mergeToLeft() }
        assertEquals(INVALID_LIST_ERROR_MESSAGE,exception.message)
    }

    @Test
    fun `mergeToLeft two different values`() {

        val row = listOf(null,GridData(value = 2),GridData(value = 4))
        val (newRow,score) = row.mergeToLeft()

        assertEquals(0,score)
        assertEquals(2,newRow.size)
        assertEquals(2,newRow[0]?.value)
        assertEquals(4,newRow[1]?.value)

    }

    @Test
    fun `mergeToLeft two identical values`() {

        val row = listOf<GridData?>(null, GridData(value = 2),null,GridData(value = 2))
        val (newRow,score) = row.mergeToLeft()

        assertEquals(4,score)
        assertEquals(4,newRow[0]?.value)
        assertEquals(1,newRow.size)

    }

}