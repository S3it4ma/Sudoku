package com.example.simplefragtest

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.simplefragtest.database.AppDatabase
import com.example.simplefragtest.database.SudokuCell
import com.example.simplefragtest.database.SudokuGame
import java.util.ArrayList
import java.util.Random
import kotlin.math.sqrt

//TODO fare sì che venga aggiunto al db il risultato della partita
class FirstFragViewModel(private val application: Application) : AndroidViewModel(application) {

    private var appDatabase : AppDatabase = AppDatabase.getDatabase(application.applicationContext)
    private var aiutiDisponibili : Int
    private var position : Int = -1
    private var nScelte : Int = 4
    private var nRighe : Int = 4

    companion object {
        const val STATO_RISOLTO = 1
        const val STATO_ATTIVO = 0
    }

    private var stato = STATO_ATTIVO


    //private val ONE_SECOND : Long = 1000
    //private var elapsedTime : MutableLiveData<Long> = MutableLiveData()
    private var initialTime : Long

    private lateinit var cells : MutableLiveData<List<SudokuCell>>
    private lateinit var elements : List<SudokuCell>

    init {
        // Update the elapsed time every second.
        val preferences = application.getSharedPreferences("data", Context.MODE_PRIVATE)
        initialTime = preferences.getLong("initialTime", 0)
        aiutiDisponibili = preferences.getInt("aiutiDisponibili", 3)
    }

    //create in ordine... ricordare
    private fun createSudoku() : List<SudokuCell> {

        val sudokuCells = ArrayList<SudokuCell>(nRighe*nRighe)

        var scelte=0
        val random = Random()

        val soluzione = SudokuSolver(nRighe).risolvi()
        val visibile = Array(nRighe) { IntArray(nRighe) }

        while (scelte < nScelte) {
            val indice = random.nextInt(soluzione.size*soluzione.size)
            val riga = indice/soluzione.size
            val colonna = indice - riga*soluzione.size

            if (visibile[riga][colonna]==0) {
                visibile[riga][colonna]=soluzione[riga][colonna]
                scelte++
            }
        }

        for (i in soluzione.indices)
            for (j in soluzione.indices) {
                sudokuCells.add(SudokuCell(i*soluzione.size+j, visibile[i][j] != 0, visibile[i][j], soluzione[i][j]))
            }
        return sudokuCells
    }

    override fun onCleared() {
        Log.d("VM", "ONCLEARED")
        super.onCleared()
    }

    private fun saveState() {
        val editor = application.getSharedPreferences("data", Context.MODE_PRIVATE).edit()

        if (stato == STATO_ATTIVO) {
            Log.d("VM", "SALVO STATO ATTIVO")

            editor.putLong("initialTime", initialTime)
            appDatabase.cellDao().deleteAll()
            appDatabase.cellDao().insertAll(elements)
        }
        else editor.clear()
        editor.apply()
    }


    fun setCell(value: Int) : Boolean {
        if (stato == STATO_RISOLTO) return false
        if (elements[position].valore == value) elements[position].valore = 0
        else elements[position].valore = value
        return true
    }

    fun getPosition() : Int {
        return position
    }
    fun getElements() : MutableLiveData<List<SudokuCell>> {
        return cells
    }
    fun setPosition(value : Int) {
        position = value
    }
    fun getInitialTime() : Long {
        return initialTime
    }
    fun getNRighe(): Int {
        return  nRighe
    }
    fun setRigheScelte(righe : Int, scelte : Int) {
        nRighe = righe
        nScelte = scelte

        initialTime = 0
        stato = STATO_ATTIVO
        elements = createSudoku()
        cells = MutableLiveData(elements)
    }
    fun getStato() : Int {
        return stato
    }
    fun setInitialTime(value : Long) {
        initialTime = value
        saveState()
    }

    fun aiuto() : Int {
        if (stato == STATO_RISOLTO || aiutiDisponibili == 0) return -1

        val random = Random()
        while (true) {
            val posizione = random.nextInt(elements.size)
            val cella = elements[posizione]
            if (!cella.iniziale) {
                aiutiDisponibili--
                cella.valore = cella.soluzione
                cella.iniziale = true
                return posizione
            }
        }
    }

    fun risolvi(timeElapsed : Long) : Boolean {
        if (stato == STATO_RISOLTO) return false

        val vinto = check()

        stato = STATO_RISOLTO
        appDatabase.cellDao().deleteAll()

        cells.postValue(elements)
        appDatabase.gameDao().insert(SudokuGame(vinto, timeElapsed/1000))

        val scritta = if (vinto) "Hai vinto!!" else "Hai perso"
        Toast.makeText(application.applicationContext, scritta, Toast.LENGTH_LONG).show()

        return vinto
    }

    private fun check() : Boolean {
        val soluzione = Array(nRighe) { IntArray(nRighe)}
        for (i in 0..<nRighe)
            for (j in 0..<nRighe)
                soluzione[i][j] = elements[i*nRighe+j].valore

        val sommaI = nRighe * (nRighe+1) / 2
        for (i in 0..<nRighe) {
            var sommaR = 0
            for (j in 0..<nRighe)
                sommaR += soluzione[i][j]
            if (sommaR!=sommaI) return false
        }
        for (j in 0..<nRighe) {
            var sommaC = 0
            for (i in 0..<nRighe)
                sommaC += soluzione[i][j]
            if (sommaC!=sommaI) return false
        }
        return true
    }

    fun azzera() {
        if (stato == STATO_RISOLTO) return

        for (cella in elements)
            if (!cella.iniziale)
                cella.valore=0

        cells.value = elements
    }

    fun prendiDaDB() {
        elements = appDatabase.cellDao().getCells()
        if (elements.isEmpty()) Log.d("VM", "è stranamente vuota")

        cells = MutableLiveData(elements)
        nRighe = sqrt(elements.size.toDouble()).toInt()
    }
}