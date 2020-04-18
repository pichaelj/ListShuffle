package com.pichaelj.listshuffle.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "shuffle_list_item_table",
    indices = [Index("id")],
    foreignKeys = [ForeignKey(
        entity = ShuffleList::class,
        parentColumns = ["id"],
        childColumns = ["list_id"],
        onDelete = ForeignKey.CASCADE)
    ])
data class ShuffleItem(
    @ColumnInfo(name = "list_id")
    val listId: Long,

    @ColumnInfo(name = "label")
    var label: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L
}