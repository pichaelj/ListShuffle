package com.pichaelj.listshuffle.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShuffleList::class, ShuffleItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getShuffleListDao(): ShuffleListDao
    abstract fun getShuffleItemDao(): ShuffleItemDao

    companion object {

        @Volatile
        private var _instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return _instance ?: synchronized(this) {
                _instance ?: buildDatabase(context).also {
                    _instance = it
                }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "list_shuffle_database")
                .fallbackToDestructiveMigration()
                .build()
    }
}