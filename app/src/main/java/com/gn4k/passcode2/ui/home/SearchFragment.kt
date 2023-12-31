package com.gn4k.passcode2.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gn4k.passcode2.R
import com.gn4k.passcode2.adapter.SearchAdapter

class SearchFragment : Fragment() {

    lateinit var view1 : View
    lateinit var rvSearch : RecyclerView
    private lateinit var searchEditText: AppCompatEditText
    private lateinit var adapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_search, container, false)

        rvSearch = view1.findViewById(R.id.rvSearch)
        searchEditText = view1.findViewById(R.id.appCompatEditText)

        setRecyclerView()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(charSequence.toString())
            }

            override fun afterTextChanged(editable: Editable?) {}
        })

        return view1
    }

    fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        adapter =
            context?.let { SearchAdapter(Home.allCategory, Home.allPassKey, Home.allPassList, it) }!!
        rvSearch.layoutManager = layoutManager
        rvSearch.adapter = adapter
    }

    companion object {

    }
}