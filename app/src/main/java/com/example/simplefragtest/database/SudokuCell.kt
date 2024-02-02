package com.example.simplefragtest.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cellTable")
data class SudokuCell(
    @PrimaryKey val pos : Int,
    @ColumnInfo(name = "iniziale") var iniziale : Boolean,
    @ColumnInfo(name = "valore") var valore : Int,
    @ColumnInfo(name = "soluzione") val soluzione : Int
)