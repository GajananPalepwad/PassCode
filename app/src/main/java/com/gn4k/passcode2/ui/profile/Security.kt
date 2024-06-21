package com.gn4k.passcode2.ui.profile

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.chaos.view.PinView
import com.gn4k.passcode2.R
import com.gn4k.passcode2.data.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Base64

class Security : AppCompatActivity() {

    lateinit var tvOldPass: PinView
    lateinit var tvNewPass: PinView
    lateinit var tvNewConfirmPass: PinView
    lateinit var passContainer: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {onBackPressed()}

        val btnChangePass: TextView = findViewById(R.id.btnChangePass)
        tvOldPass = findViewById(R.id.tvOldPass)
        tvNewPass = findViewById(R.id.tvNewPass)
        tvNewConfirmPass = findViewById(R.id.tvNewConfirmPass)

        val btnSetPass: Button = findViewById(R.id.btnSetPass)
        passContainer = findViewById(R.id.passContainer)


        btnChangePass.setOnClickListener {
            if (passContainer.visibility == View.VISIBLE) {
                passContainer.visibility = View.GONE
            } else {
                passContainer.visibility = View.VISIBLE
            }
        }

        btnSetPass.setOnClickListener {
            if (tvOldPass.text.toString().isNotEmpty() &&
                tvNewPass.text.toString().isNotEmpty() &&
                tvNewConfirmPass.text.toString().isNotEmpty()) {
                showProgress()
                compareWithOldPass(tvOldPass.text.toString())
            }
        }


    }

    private fun compareWithOldPass(password: String){
        val database = FirebaseDatabase.getInstance()
        val userRef: DatabaseReference = database.getReference("users").child(loadLocalData()).child("details")

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(User::class.java)
                    val encodedPass = user?.encodedPass
                    val decodedBytes = Base64.getDecoder().decode(encodedPass)
                    val decodedPass = String(decodedBytes, Charsets.UTF_8)

                    if (decodedPass.equals(password)) {

                        if(tvNewPass.text.toString().equals(tvNewConfirmPass.text.toString())){
                            val encodedPass = Base64.getEncoder().encodeToString(tvNewPass.text.toString().toByteArray())
                            setNewPassword(encodedPass)
                        }else{
                            Toast.makeText(applicationContext, "New Passwords NOT Matched", Toast.LENGTH_SHORT).show()
                            hideProgress()
                        }

                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Please Enter correct password",
                            Toast.LENGTH_SHORT
                        ).show()
                        hideProgress()
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                hideProgress()
            }
        })
    }

    fun setNewPassword(newPassword: String){
        val database = FirebaseDatabase.getInstance()

        val myRef = database.getReference("users/${loadLocalData()}/details/encodedPass")

        myRef.setValue(newPassword)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                tvOldPass.setText("")
                tvNewPass.setText("")
                tvNewConfirmPass.setText("")
                hideProgress()
                passContainer.visibility = View.GONE
            }
            .addOnFailureListener { error ->
                hideProgress()
                Toast.makeText(applicationContext, "Failed to update: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun loadLocalData(): String {
        val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        return sharedPreferences?.getString("userName", null).toString()
    }

    fun showProgress(){
        findViewById<ProgressBar>(R.id.pro).visibility = View.VISIBLE
    }
    fun hideProgress(){
        findViewById<ProgressBar>(R.id.pro).visibility = View.GONE
    }

}