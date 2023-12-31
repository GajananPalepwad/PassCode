package com.gn4k.passcode2.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gn4k.passcode2.R
import com.gn4k.passcode2.adapter.CategoryAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    lateinit var view1: View
    val categoryList = mutableListOf<String>()
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_home, container, false)
        getCategoryList();

        recyclerView = view1.findViewById(R.id.rvList)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        return view1
    }

    private fun getCategoryList() {

        Home.allPassList.clear()
        Home.allCategory.clear()
        Home.allPassKey.clear()
        val databaseReference = FirebaseDatabase.getInstance().reference
        // Reference to the specific node you want to retrieve children from
        val userNodeReference = databaseReference.child("users").child(loadLocalData())


        userNodeReference.child("password").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Handle data change
                val passwordMapType = object : GenericTypeIndicator<Map<String, Any>>() {}
                val passwordMap = snapshot.getValue(passwordMapType)
                if (passwordMap != null) {

                    // Iterate through the top-level children
                    for (categorySnapshot in snapshot.children) {
                        val categoryKey = categorySnapshot.key
                        categoryList.add(categoryKey ?: "")
                    }
                    val adapter = context?.let { CategoryAdapter(categoryList, it) }

                    recyclerView.adapter = adapter

                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
    }

    fun loadLocalData() : String{

        val sharedPreferences = context?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("userName", null).toString()
        return id
    }

    companion object {

    }
}