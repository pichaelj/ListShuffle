package com.pichaelj.listshuffle

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pichaelj.listshuffle.data.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShuffleItemDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var listDao: ShuffleListDao
    private lateinit var itemDao: ShuffleItemDao

    private var testList = ShuffleList("TestList")

    private val expectedItemCount = 3

    // Late init because they need the testList id for foreign key constraint
    private lateinit var itemA: ShuffleItem
    private lateinit var itemB: ShuffleItem
    private lateinit var itemC: ShuffleItem

    private val testListId: Long
        get() = testList.id

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        listDao = db.getShuffleListDao()
        itemDao = db.getShuffleItemDao()

        testList.id = listDao.insert(testList)

        itemA = ShuffleItem(testListId, "itemA")
        itemB = ShuffleItem(testListId, "itemB")
        itemC = ShuffleItem(testListId, "itemC")

        // Insert in non-alpha order
        itemA.id = itemDao.insert(itemA)
        itemB.id = itemDao.insert(itemB)
        itemC.id = itemDao.insert(itemC)
    }

    @After
    fun closeDb(){
        db.close()
    }

    @Test
    fun testGetItems() {
        // Items should be returned in alpha order
        val items = getValue(itemDao.getItems(testList.id))
        assertEquals(expectedItemCount, items.size)
        assertEquals(items[0], itemA)
        assertEquals(items[1], itemB)
        assertEquals(items[2], itemC)
    }

    @Test
    fun testDeleteItem() {
        // Delete a single item
        itemDao.delete(itemA)

        // Ensure it was deleted
        val items = getValue(itemDao.getItems(testListId))
        assertEquals(expectedItemCount - 1, items.size)

        assertEquals(itemB, items[0])
        assertEquals(itemC, items[1])
    }

    @Test
    fun testDeleteList() {
        // Deleting a list should result in the deletion of all of its items.
        listDao.delete(testList)

        val items = getValue(itemDao.getItems(testListId))
        assertEquals(0, items.size)
    }
}