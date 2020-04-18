package com.pichaelj.listshuffle.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "shuffle_list_table", indices = [Index("id")])
data class ShuffleList(
    @ColumnInfo(name = "name")
    var name: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L
}