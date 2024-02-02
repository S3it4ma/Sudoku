package com.example.simplefragtest.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.simplefragtest.database.SudokuGame

@Dao
interface SudokuGameDAO {
    @Insert
    fun insert(g : SudokuGame)

    @Query("DELETE FROM gameTable")
    fun deleteAllGames()

    @Query("DELETE FROM sqlite_sequence WHERE name = 'gameTable'")
    fun deleteID()

    @Transaction
    fun deletAll() {
        deleteAllGames()
        deleteID()
    }

    @Query("SELECT * FROM gameTable ORDER BY pos DESC")
    fun getGames() : List<SudokuGame>

    @Query("SELECT count(*) FROM gameTable WHERE vinta = 1")
    fun getNumberOfGamesWon() : Int

    @Query("SELECT min(tempo) FROM gameTable WHERE vinta = 1")
    fun getMinTimeWon() : Long
}