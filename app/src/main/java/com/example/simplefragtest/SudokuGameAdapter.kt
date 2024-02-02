package com.example.simplefragtest

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.simplefragtest.database.SudokuGame

class SudokuGameAdapter() :
    RecyclerView.Adapter<SudokuGameAdapter.ViewHolder>() {

    private lateinit var data : List<SudokuGame>

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var partita : TextView = view.findViewById(R.id.partita)
        var esito : TextView = view.findViewById(R.id.esito)
        var tempo : TextView = view.findViewById(R.id.tempo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sudoku_game_view,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setGames(newD : List<SudokuGame>) {
        data = newD
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sudokuGame = data[position]
        Log.d("GameAdapter", "il valore di vinta è ${sudokuGame.vinta}")

        val minuti = (sudokuGame.tempo / 60).toInt()
        val secondi = (sudokuGame.tempo - minuti * 60).toInt()
        holder.partita.text = sudokuGame.pos.toString()
        if (sudokuGame.vinta) {
            holder.esito.text = "vinta"
            holder.esito.setTextColor(Color.GREEN)
        }
        else {
            holder.esito.text = "persa"
            holder.esito.setTextColor(Color.RED)
        }
        holder.tempo.text = "$minuti min $secondi s"
    }
}