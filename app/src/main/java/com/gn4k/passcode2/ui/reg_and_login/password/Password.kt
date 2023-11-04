package com.gn4k.passcode2.ui.reg_and_login.password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.gn4k.passcode2.R
import com.gn4k.passcode2.ui.home.Home
import com.gn4k.passcode2.ui.onboard.Onboard

class Password : AppCompatActivity() {

    var transaction = supportFragmentManager.beginTransaction()
    lateinit var btnEnter: Button

    lateinit var whichFragment: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        btnEnter = findViewById<Button>(R.id.btnSetPass)

        whichFragment = intent.getStringExtra("key").toString()


        btnEnter.setOnClickListener {
            if(whichFragment.equals("log")){
                Toast.makeText(applicationContext, EnterPassword.pass.text, Toast.LENGTH_SHORT).show()

            }else if(whichFragment.equals("reg")){

                val confopass = SetPassword.confoPass.text.toString()
                val pass = SetPassword.pass.text.toString()


                if(confopass.equals(pass)){
                    Toast.makeText(applicationContext, "Successfully", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(applicationContext, "$confopass Matched $pass", Toast.LENGTH_SHORT).show()
                }

            }

            intent = Intent(applicationContext, Home::class.java)
            startActivity(intent)
        }
        setFragment()
    }

    private fun setFragment(){
        transaction = supportFragmentManager.beginTransaction()

        if(whichFragment.equals("log")){
            transaction.replace(R.id.container, EnterPassword())

        }else if(whichFragment.equals("reg")){
            transaction.replace(R.id.container, SetPassword())

        }

        transaction.addToBackStack(null) // Add the transaction to the back stack if needed
        transaction.commit()
    }
}