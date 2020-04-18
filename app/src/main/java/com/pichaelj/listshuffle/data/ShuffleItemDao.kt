package com.pichaelj.listshuffle.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShuffleItemDao {

    @Query("SELECT * FROM shuffle_list_item_table WHERE list_id=:listId ORDER BY label")
    fun getItems(listId: Long): LiveData<List<ShuffleItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ShuffleItem): Long

    @Delete
    fun delete(item: ShuffleItem)
}