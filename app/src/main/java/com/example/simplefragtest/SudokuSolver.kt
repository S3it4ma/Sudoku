package com.example.simplefragtest

import java.util.Collections
import kotlin.math.sqrt

class SudokuSolver(n: Int) {
    private val solution: Array<IntArray>
    private val returnSol: Array<IntArray>
    private val scelte = ArrayList<Int>()
    private var trovato = false

    init {
        for (i in 0 until n) scelte.add(i + 1)
        solution = Array(n) { IntArray(n) }
        returnSol= Array(n) { IntArray(n) }
    }

    fun risolvi(): Array<IntArray> {
        Collections.shuffle(scelte)
        val primaCella = 0
        tentativo(primaCella)
        return returnSol
    }

    private fun scriviSoluzione() {
        for(i in solution.indices) {
            for(j in solution.indices)
                 returnSol[i][j] = solution[i][j]
        }
    }

    private fun coordinate(cella: Int): IntArray {
        val r = cella / solution[0].size
        val c = cella - r * solution.size
        return intArrayOf(r, c)
    }

    private fun tentativo(cella: Int) {
        val coord = coordinate(cella)
        val r = coord[0]
        val c = coord[1]
        for (s in scelte) {
            if (trovato) break
            if (verificaRC(r, c, s) && verificaG(r, c, s)) {
                solution[r][c] = s
                if (cella == solution.size * solution.size - 1) {
                    scriviSoluzione()
                    trovato = true
                }
                else tentativo(cella + 1)
                solution[r][c] = 0
            }
        }
    }

    private fun verificaRC(riga: Int, colonna: Int, v: Int): Boolean {
        for (i in solution.indices) {
            if (solution[i][colonna] == v || solution[riga][i] == v) return false
        }
        return true
    }

    private fun verificaG(r: Int, c: Int, v: Int): Boolean {
        val grandezzaGruppo = sqrt(solution.size.toDouble()).toInt()
        val m = r / grandezzaGruppo * grandezzaGruppo
        val n = c / grandezzaGruppo * grandezzaGruppo
        for (i in m until m + grandezzaGruppo) {
            for (j in n until n + grandezzaGruppo) {
                if (solution[i][j] == v) return false
            }
        }
        return true
    }
}