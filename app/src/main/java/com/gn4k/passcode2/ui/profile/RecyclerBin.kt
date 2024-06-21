package com.gn4k.passcode2.ui.profile

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gn4k.passcode2.R
import com.gn4k.passcode2.adapter.RecyclerBinCategoryAdapter
import com.gn4k.passcode2.ui.other.Other
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

class RecyclerBin : AppCompatActivity() {

    val categoryList = mutableListOf<String>()
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_bin)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {onBackPressed()}

        pro = findViewById(R.id.pro)


        getCategoryList();
        recyclerView = findViewById(R.id.rvList)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
    }

    private fun getCategoryList() {

        Other.allPassList.clear()
        Other.allCategory.clear()
        Other.allPassKey.clear()
        val databaseReference = FirebaseDatabase.getInstance().reference
        // Reference to the specific node you want to retrieve children from
        val userNodeReference = databaseReference.child("users").child(loadLocalData())


        userNodeReference.child("deletedPassword").addListenerForSingleValueEvent(object :
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
                    val adapter = RecyclerBinCategoryAdapter(categoryList, applicationContext)

                    recyclerView.adapter = adapter
                }
                pro.visibility = View.GONE

            }

            override fun onCancelled(error: DatabaseError) {
                pro.visibility = View.GONE
            }
        })
    }

    fun loadLocalData() : String{

        val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("userName", null).toString()
        return id
    }

    companion object{
        lateinit var pro : ProgressBar;
    }

}