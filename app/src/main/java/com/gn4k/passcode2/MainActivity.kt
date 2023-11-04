package com.gn4k.passcode2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gn4k.passcode2.ui.home.Home
import com.gn4k.passcode2.ui.onboard.Onboard
import com.gn4k.passcode2.ui.reg_and_login.RegistrationAndLogin

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intent = Intent(applicationContext, Onboard::class.java)
        startActivity(intent)

    }
}