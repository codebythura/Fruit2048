package com.codebythura.fruit2048.db


import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.codebythura.fruit2048.data.GRID_SIZE
import com.codebythura.fruit2048.data.GridData
import com.codebythura.fruit2048.database.AppDatabase
import com.codebythura.fruit2048.database.GameStateDao
import com.codebythura.fruit2048.database.GameStateEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var appDatabase : AppDatabase
    private lateinit var gameStateDao: GameStateDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder<AppDatabase>(context)
            .setDriver(BundledSQLiteDriver())
            .build()
        gameStateDao = appDatabase.gameStateDao
    }

    @After
    fun after() {
        appDatabase.close()
    }

    @Test
    fun deleteAllGameStatesAndCheckTheRowCount() = runTest {

        gameStateDao.insertGameState(createGameState(id = 1))
        gameStateDao.insertGameState(createGameState(id = 2))

        gameStateDao.deleteAllGameStates()

        assertNull(gameStateDao.getLatestState())

    }

    @Test
    fun insertEntitiesAndObserveRowCount() = runTest {

        gameStateDao.observeRowCount().test {

            assertEquals(0,awaitItem())

            gameStateDao.insertGameState(createGameState(id = 1, score = 0))
            assertEquals(1,awaitItem())

            gameStateDao.insertGameState(createGameState(id = 2, score = 0))
            assertEquals(2,awaitItem())

        }

    }

    @Test
    fun checkInitialState() = runTest {
        assertNull(gameStateDao.getLatestState())
    }

    @Test
    fun insertNewEntityAndCheckIt() = runTest {

        val first = createGameState(id = 1, score = 20)
        val second = createGameState(id = 2,score = 0)

        gameStateDao.insertGameState(first)
        gameStateDao.insertGameState(second)

        assertEquals(gameStateDao.getLatestState()?.id,second.id)

    }

    private fun createGameState(id : Int,score : Int = 0) : GameStateEntity {
        return GameStateEntity(
            id = id,
            score = score,
            isOver = false,
            state = listOf(
                listOf(null, GridData(value = 2),GridData(value = 2),GridData(value = 2),null),
                List(GRID_SIZE) { null },
                List(GRID_SIZE) { null },
                List(GRID_SIZE) { null },
            ),
        )
    }



}