package com.example.simplefragtest.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.simplefragtest.database.SudokuCell

@Dao
interface SudokuCellDAO {
    @Insert
    fun insert(c : SudokuCell)

    @Insert
    fun insertAll(cells : List<SudokuCell>)

    @Update
    fun update(c : SudokuCell)

    @Query("DELETE FROM cellTable")
    fun deleteAll()

    @Query("SELECT * FROM cellTable ORDER BY pos ASC")
    fun getCells() : List<SudokuCell>
}