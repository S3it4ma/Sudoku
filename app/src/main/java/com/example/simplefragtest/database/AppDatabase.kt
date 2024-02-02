package com.example.simplefragtest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SudokuCell::class, SudokuGame::class], version = 8)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cellDao() : SudokuCellDAO
    abstract fun gameDao() : SudokuGameDAO

    companion object {
        private var instance : AppDatabase? = null

        @Synchronized
        fun getDatabase(context : Context) : AppDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "cellDatabase")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .apply {
                    instance = this
                }
        }
    }
}