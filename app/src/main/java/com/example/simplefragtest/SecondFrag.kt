package com.example.simplefragtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplefragtest.database.AppDatabase
import com.example.simplefragtest.database.SudokuGame

class SecondFrag : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val gameDatabase = context?.let { AppDatabase.getDatabase(it).gameDao() }

        val sudokuGames = gameDatabase?.getGames()

        val view = inflater.inflate(R.layout.fragment_second, container, false)

        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerViewPartite)
        val layoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager=layoutManager

        val adapter = SudokuGameAdapter()
        if (sudokuGames != null) {
            adapter.setGames(sudokuGames)
        }
        recyclerView.adapter = adapter


        val numPartite : TextView = view.findViewById(R.id.numPartite)
        val partite = sudokuGames?.size ?: 0
        val vinte = gameDatabase?.getNumberOfGamesWon() ?: 0
        numPartite.text = "$vinte/$partite"

        val progressBar : ProgressBar = view.findViewById(R.id.progressBar)
        progressBar.max = partite
        progressBar.progress = vinte

        val tempoMin : TextView = view.findViewById(R.id.tempo_min)
        val tempo = gameDatabase?.getMinTimeWon() ?: 0
        val minuti = (tempo / 60).toInt()
        val secondi = (tempo - minuti * 60).toInt()
        tempoMin.text = "$minuti min $secondi s"

        val cancella : Button = view.findViewById(R.id.cancella)
        cancella.setOnClickListener {
            gameDatabase?.deletAll()
            adapter.setGames(ArrayList())
            numPartite.text = "0/0"
            tempoMin.text = "0 min 0 s"
            progressBar.progress = 0
        }
        view.refreshDrawableState()
        return view
    }

}