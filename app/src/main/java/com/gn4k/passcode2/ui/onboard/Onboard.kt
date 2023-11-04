package com.gn4k.passcode2.ui.onboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.gn4k.passcode2.R
import com.gn4k.passcode2.ui.reg_and_login.RegistrationAndLogin

class Onboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard)

        var btnReg = findViewById<Button>(R.id.btnRegister)
        var btnLogin = findViewById<Button>(R.id.btnLogin)


        btnReg.setOnClickListener {
            intent = Intent(applicationContext, RegistrationAndLogin::class.java)
            intent.putExtra("key", "reg");
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            intent = Intent(applicationContext, RegistrationAndLogin::class.java)
            intent.putExtra("key", "log");
            startActivity(intent)
        }

    }
}