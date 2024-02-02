package com.example.simplefragtest


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.simplefragtest.database.SudokuCell


class SudokuCellAdapter() :
    RecyclerView.Adapter<SudokuCellAdapter.ViewHolder>() {

    companion object {
        const val STATO_RISOLTO = 1
        const val STATO_ATTIVO = 0
    }
    private var vinto = false
    private var stato = STATO_ATTIVO
    private lateinit var data : List<SudokuCell>
    private lateinit var listener : OnItemClickListener

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val textView : TextView = view.findViewById(R.id.sudoku_text)
        init {
            view.setOnClickListener {
                val dataPosition = absoluteAdapterPosition
                if (dataPosition != RecyclerView.NO_POSITION)
                    listener.onItemClick(data[dataPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.sudoku_cell_view,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setCells(newD : List<SudokuCell>) {
        data = newD
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sudokuCell = data[position]
        
        if(sudokuCell.iniziale) {
            holder.textView.background = ContextCompat.getDrawable(holder.textView.context, R.drawable.sudoku_cell_filled)
        } else {
            holder.textView.background = ContextCompat.getDrawable(holder.textView.context, R.drawable.sudoku_cell)
        }
        showErrors(holder, sudokuCell)

        if(stato == STATO_RISOLTO) holder.textView.text = sudokuCell.soluzione.toString()

        else {
            if(sudokuCell.valore==0) holder.textView.text=""
            else holder.textView.text = sudokuCell.valore.toString()
        }
    }

    fun setItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    private fun showErrors(holder: ViewHolder, cell : SudokuCell) {
        if (stato == STATO_ATTIVO || cell.iniziale) return

        if (vinto || cell.valore == cell.soluzione)
            holder.textView.background = ContextCompat.getDrawable(holder.textView.context, R.drawable.sudoku_cell_green)
        else {
            holder.textView.background = ContextCompat.getDrawable(holder.textView.context, R.drawable.sudoku_cell_red)
        }
    }

    fun setViewMode(stato : Int) {
        this.stato = stato
    }

    fun setVinto(vinto : Boolean) {
        this.vinto= vinto
    }

    interface OnItemClickListener {
        fun onItemClick(cell : SudokuCell)
    }
}