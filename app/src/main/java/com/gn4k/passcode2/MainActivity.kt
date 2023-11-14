package com.gn4k.passcode2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gn4k.passcode2.ui.home.Home
import com.gn4k.passcode2.ui.onboard.Onboard
import com.gn4k.passcode2.ui.reg_and_login.RegistrationAndLogin
import com.gn4k.passcode2.ui.reg_and_login.password.Password

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val id = loadLocalData()

        if(id == null || id.equals("") || id.equals("null")) {
            intent = Intent(applicationContext, Onboard::class.java)
            startActivity(intent)
        } else {
            intent = Intent(applicationContext, Password::class.java)
            intent.putExtra("key", "saved")
            startActivity(intent)
        }

    }

    fun loadLocalData() : String{
        val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("userName", null).toString()
        return id
    }

}