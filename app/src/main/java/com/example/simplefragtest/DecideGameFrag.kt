package com.example.simplefragtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.simplefragtest.database.AppDatabase

class DecideGameFrag : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_decide_game, container, false)

        val viewModel = ViewModelProvider(requireActivity())[FirstFragViewModel::class.java]

        val riprendi : Button = view.findViewById(R.id.riprendi)
        riprendi.setOnClickListener {
            viewModel.prendiDaDB()
            goToPlay()
        }

        if (context?.let { AppDatabase.getDatabase(it).cellDao().getCells().isEmpty() } == true)
            riprendi.isEnabled = false

        val facile : Button = view.findViewById(R.id.facile)
        facile.setOnClickListener {
            viewModel.setRigheScelte(9,50)
            goToPlay()
        }
        val medio : Button = view.findViewById(R.id.medio)
        medio.setOnClickListener {
            viewModel.setRigheScelte(9,35)
            goToPlay()
        }
        val difficile : Button = view.findViewById(R.id.difficile)
        difficile.setOnClickListener {
            viewModel.setRigheScelte(9,25)
            goToPlay()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun goToPlay() {
        val action = DecideGameFragDirections.actionNavigationDecidePlayToNavigationPlay()
        findNavController().navigate(action)
    }
}