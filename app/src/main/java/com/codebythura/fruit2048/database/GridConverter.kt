package com.codebythura.fruit2048.database

import androidx.room.TypeConverter
import com.codebythura.fruit2048.data.GRID_SIZE
import com.codebythura.fruit2048.data.GridData
import com.codebythura.fruit2048.data.GridMatrix

class GridConverter {

    @TypeConverter
    fun fromBoard(grid: GridMatrix): String {
        val result = mutableListOf<String>()
        for (row in 0 until GRID_SIZE) {
            for (col in 0 until GRID_SIZE) {
                val value: GridData = grid[row][col] ?: continue
                result.add("($value,$row,$col)")
            }
        }
        return result.joinToString(":")
    }

    @TypeConverter
    fun toBoard(json: String): GridMatrix {
        val list = json.trim().split(":")
        val grid : List<MutableList<GridData?>> = MutableList(GRID_SIZE) { MutableList(GRID_SIZE) { null } }
        for (data in list) {
            val (value, row, col) = data.removePrefix("(").removeSuffix(")").split(",")
            grid[row.toInt()][col.toInt()] = GridData(value = value.toInt(), shouldAnimated = false)
        }
        return grid
    }

}