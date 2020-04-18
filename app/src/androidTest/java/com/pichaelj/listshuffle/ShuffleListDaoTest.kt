package com.pichaelj.listshuffle

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pichaelj.listshuffle.data.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class ShuffleListDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var listDao: ShuffleListDao

    private val expectedListCount = 3
    private var listA = ShuffleList("TestListA")
    private var listB = ShuffleList("TestListB")
    private var listC = ShuffleList("TestListC")

    @Before
    @Throws(Exception::class)
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        listDao = db.getShuffleListDao()

        // Insert in non-alpha order
        listB.id = listDao.insert(listB)
        listA.id = listDao.insert(listA)
        listC.id = listDao.insert(listC)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testGetLists() {
        // Items should be returned in alpha order
        val lists = getValue(listDao.getLists())
        assertEquals(expectedListCount, lists.size)
        assertEquals(listA, lists[0])
        assertEquals(listB, lists[1])
        assertEquals(listC, lists[2])
    }

    @Test
    fun testDelete() {
        // Delete a single list
        listDao.delete(listA)

        // Ensure it was deleted
        val lists = getValue(listDao.getLists())
        assertEquals(expectedListCount - 1, lists.size)

        assertEquals(listB, lists[0])
        assertEquals(listC, lists[1])
    }

    @Test
    fun testClear() {
        // Delete all lists
        listDao.clear()

        // There should be no lists
        val lists = getValue(listDao.getLists())
        assertEquals(0, lists.size)
    }
}
