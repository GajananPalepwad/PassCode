package com.gn4k.passcode2.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gn4k.passcode2.R
import com.gn4k.passcode2.adapter.AnalysAdapter
import com.gn4k.passcode2.adapter.PassDataAdapter


class AnalysisFragment : Fragment() {


    lateinit var view1: View
    lateinit var rcAnalysis: RecyclerView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_analysis, container, false)

        rcAnalysis = view1.findViewById(R.id.rcAnalysis)
        tvSafe = view1.findViewById(R.id.tvSafe)
        tvWeak = view1.findViewById(R.id.tvWeak)
        tvRisk = view1.findViewById(R.id.tvRisk)
        tvSecuredNumber = view1.findViewById(R.id.tvSecuredNumber)
        tvSecured = view1.findViewById(R.id.tvSecured)
        progressSecured = view1.findViewById(R.id.progressSecured)

        setRecyclerView()

        return view1
    }

    fun setRecyclerView() {
        safe = 0
        weak = 0
        risk = 0
        val layoutManager = LinearLayoutManager(context)
        val adapter =
            context?.let { AnalysAdapter(Home.allCategory, Home.allPassKey, Home.allPassList, it) }
        rcAnalysis.layoutManager = layoutManager
        rcAnalysis.adapter = adapter
    }

    companion object {
        var safe = 0
        var weak = 0
        var risk= 0
        lateinit var tvRisk: TextView
        lateinit var tvSafe: TextView
        lateinit var tvWeak: TextView
        lateinit var tvSecuredNumber: TextView
        lateinit var tvSecured: TextView
        lateinit var progressSecured: ProgressBar

    }
}