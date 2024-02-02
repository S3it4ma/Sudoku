package com.example.simplefragtest

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast


class ThirdFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_third, container, false)

        val preferences = context?.getSharedPreferences("data", Context.MODE_PRIVATE)
        val modalitaAutomatica : Switch = view.findViewById(R.id.modalitaAutomatica)

        modalitaAutomatica.isChecked = preferences?.getBoolean("autoMode", true) ?: true
        modalitaAutomatica.setOnCheckedChangeListener { compoundButton, isChecked ->
            val editor = preferences?.edit()
            if (compoundButton?.isPressed == true) {
                editor?.putBoolean("autoMode", isChecked)
                editor?.apply()
                Log.d("SettingsFrag","$isChecked")
            }
        }

        return view
    }

}