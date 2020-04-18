package com.pichaelj.listshuffle.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ShuffleListDao {

    @Query("SELECT * FROM shuffle_list_table ORDER BY name")
    fun getLists(): LiveData<List<ShuffleList>>

    @Insert
    fun insert(list: ShuffleList): Long

    @Delete
    fun delete(list: ShuffleList)

    @Query("DELETE FROM shuffle_list_table")
    fun clear()
}