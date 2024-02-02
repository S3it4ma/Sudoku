package com.example.simplefragtest.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gameTable")
data class SudokuGame (
    @PrimaryKey(autoGenerate = true) val pos : Int =  0,
    @ColumnInfo(name = "vinta") val vinta : Boolean,
    @ColumnInfo(name = "tempo") var tempo : Long,
) {
    constructor(vinta: Boolean, tempo: Long) : this(0, vinta, tempo)
}