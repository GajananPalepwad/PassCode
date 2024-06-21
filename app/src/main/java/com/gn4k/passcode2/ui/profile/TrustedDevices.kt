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
import com.gn4k.passcode2.adapter.BlockedDevicesAdapter
import com.gn4k.passcode2.adapter.TrustedDevicesAdapter
import com.gn4k.passcode2.data.DeviceLoginDetail
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TrustedDevices : AppCompatActivity() {

    private lateinit var rvListLogged: RecyclerView
    private lateinit var rvListBlocked: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trusted_devices)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {onBackPressed()}

        pro = findViewById(R.id.pro)

        rvListLogged = findViewById(R.id.rvListLogged)
        rvListBlocked = findViewById(R.id.rvListBlocked)

        rvListLogged.layoutManager = LinearLayoutManager(this)
        rvListBlocked.layoutManager = LinearLayoutManager(this)

        retrieveLoggedInDevices()
        retrieveBlockedDevices()
    }

    private fun retrieveLoggedInDevices() {
        // Assuming you have a reference to your Firebase Database
        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(loadLocalData()).child("loginDetails").child("loggedInDevices")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val loginDetails = mutableListOf<DeviceLoginDetail>()
                for (data in snapshot.children) {
                    val device = data.key
                    val time = data.value as String // Assuming time is stored as a String
                    if (device != null) {
                        val detail = DeviceLoginDetail(device, time)
                        loginDetails.add(detail)
                    }
                }
                val adapter = TrustedDevicesAdapter(loginDetails, applicationContext)
                rvListLogged.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

    }


    private fun retrieveBlockedDevices() {
        // Assuming you have a reference to your Firebase Database
        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(loadLocalData()).child("loginDetails").child("blockedDevice")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val loginDetails = mutableListOf<DeviceLoginDetail>()
                for (data in snapshot.children) {
                    val device = data.key
                    val time = data.value as String // Assuming time is stored as a String
                    if (device != null) {
                        val detail = DeviceLoginDetail(device, time)
                        loginDetails.add(detail)
                    }
                }
                val adapter = BlockedDevicesAdapter(loginDetails, applicationContext)
                rvListBlocked.adapter = adapter
                hideProgress()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

    }

    fun loadLocalData() : String{

        val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("userName", null).toString()
        return id
    }

    fun showProgress(){
        findViewById<ProgressBar>(R.id.pro).visibility = View.VISIBLE
    }
    fun hideProgress(){
        findViewById<ProgressBar>(R.id.pro).visibility = View.GONE
    }

    companion object{
        lateinit var pro : ProgressBar;
    }

}
