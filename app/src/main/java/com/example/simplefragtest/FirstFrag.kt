package com.example.simplefragtest

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplefragtest.database.SudokuCell

// remember to make parametric with respect to number of rows/columns
class FirstFrag : Fragment() {

    //TODO : spostare tutta la logica del sudokuSolver IN VIEWMODEL e usare i liveData

    private lateinit var viewModel : FirstFragViewModel
    private lateinit var view : View
    private lateinit var chrono : Chronometer

    companion object {
        var RIPRENDI = 0
        var FACILE = 1
        var MEDIO = 2
        var DIFFICILE = 3
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        view = inflater.inflate(R.layout.fragment_first, container, false)

        viewModel = ViewModelProvider(requireActivity())[FirstFragViewModel::class.java]

        // populate RecyclerView
        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerView)
        val layoutManager = GridLayoutManager(view.context, viewModel.getNRighe())
        recyclerView.layoutManager=layoutManager

        val adapter=SudokuCellAdapter()
        viewModel.getElements().value?.let { adapter.setCells(it) }

        adapter.setItemClickListener(object : SudokuCellAdapter.OnItemClickListener {
            override fun onItemClick(cell: SudokuCell) {
                if (cell.iniziale) return
                viewModel.setPosition(cell.pos)
            }
        })
        recyclerView.adapter=adapter

        //subscribe to viewModel cells
        viewModel.getElements().observe(viewLifecycleOwner) {

            if (viewModel.getStato() == FirstFragViewModel.STATO_RISOLTO) {
                adapter.setViewMode(SudokuCellAdapter.STATO_RISOLTO)
            }
            adapter.notifyItemRangeChanged(0, adapter.itemCount)
        }

        // setup timer
        chrono = view.findViewById(R.id.chrono)
        chrono.base = SystemClock.elapsedRealtime() - viewModel.getInitialTime()
        chrono.start()

        // setup buttons for sudoku management
        setupCellButtons(adapter, view)

        val azzera : Button = view.findViewById(R.id.azzera)
        azzera.setOnClickListener {
            viewModel.azzera()
        }

        val aiuto : Button = view.findViewById(R.id.aiuto)
        aiuto.setOnClickListener {
            val posizione = viewModel.aiuto()
            if (posizione != -1) adapter.notifyItemChanged(posizione)
        }

        val risolvi : Button = view.findViewById(R.id.risolvi)
        risolvi.setOnClickListener {
            chrono.stop()
            val timeElapsed = SystemClock.elapsedRealtime() - chrono.base
            adapter.setVinto(viewModel.risolvi(timeElapsed))
        }

        view.refreshDrawableState()
        return view
    }


    fun setupCellButtons(adapter : SudokuCellAdapter, view : View) {
        val ll : LinearLayout = view.findViewById(R.id.layout_numeri)

        for (i in 1..9) {
            val params  = LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f)
            val button = Button(view.context)
            with (button) {
                id=i
                text=i.toString()
                //setBackgroundColor(Color.rgb(0,0,0))
                setOnClickListener {
                    if (viewModel.getPosition()!=-1) {

                        if (viewModel.setCell(i))
                            adapter.notifyItemChanged(viewModel.getPosition())

                    }
                }
            }
            ll.addView(button, params)
        }
    }

    override fun onPause() {
        Log.d("FirstFrag", "sto in pausa")
        val timeElapsed = SystemClock.elapsedRealtime() - chrono.base
        viewModel.setInitialTime(timeElapsed)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        chrono.base = SystemClock.elapsedRealtime() - viewModel.getInitialTime()
        if (viewModel.getStato() == FirstFragViewModel.STATO_RISOLTO) chrono.stop()
    }

    override fun onDestroy() {
        Log.d("FirstFrag", "onDestroy")
        super.onDestroy()
    }
}