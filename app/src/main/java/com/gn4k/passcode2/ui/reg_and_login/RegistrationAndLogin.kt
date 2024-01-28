package com.gn4k.passcode2.ui.reg_and_login

import java.util.Base64
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentContainerView
import com.gn4k.passcode2.R
import com.gn4k.passcode2.ui.onboard.Onboard
import com.gn4k.passcode2.ui.reg_and_login.password.Password

class RegistrationAndLogin : AppCompatActivity() {

    private lateinit var fragmentContainer: FragmentContainerView
    lateinit var btnSetPass : Button
    lateinit var btnReg : Button
    lateinit var btnLog : Button

    lateinit var whichFragment: String

    var transaction = supportFragmentManager.beginTransaction()

    val registrationFragment = Registration()
    val loginFragment = Login()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_and_login)

        fragmentContainer = findViewById(R.id.container)

        btnSetPass = findViewById(R.id.btnSetPass)
        btnLog = findViewById(R.id.btnLogin)
        btnReg = findViewById(R.id.btnRegister)


        whichFragment = intent.getStringExtra("key").toString()

        setFragment()


        btnSetPass.setOnClickListener {

            val intent = Intent(applicationContext, Password::class.java )
            intent.putExtra("key", whichFragment)
            if(whichFragment.equals("reg")){
                intent.putExtra("firstName", Registration.edFirstName.text.toString())
                intent.putExtra("lastName", Registration.edLastName.text.toString())
                intent.putExtra("email", Registration.edEmail.text.toString())

                if(!Registration.edFirstName.text.toString().isEmpty() && !Registration.edLastName.text.toString().isEmpty() && !Registration.edLastName.text.toString().isEmpty()) {
                    startActivity(intent)
                }else{
                    Toast.makeText(applicationContext, "Fill the all fields", Toast.LENGTH_SHORT).show()
                }

            } else if(whichFragment.equals("log")){

                intent.putExtra("email", Login.edEmail.text.toString())
                if(!Login.edEmail.text.toString().isEmpty()){
                    startActivity( intent )
                }else{
                    Toast.makeText(applicationContext, "Fill the all fields", Toast.LENGTH_SHORT).show()
                }

            }

        }

        btnLog.setOnClickListener {
            whichFragment = "log"
            setFragment()
        }

        btnReg.setOnClickListener {
            whichFragment = "reg"
            setFragment()

        }

    }

    private fun changeButtonTheme(btn: Button, backgroundTint: Int, textColor: Int){
        btn.setBackgroundColor(backgroundTint)
        btn.setTextColor(textColor)
    }

    private fun setFragment(){
        transaction = supportFragmentManager.beginTransaction()

        if(whichFragment.equals("log")){
            transaction.replace(R.id.container, loginFragment)
            btnSetPass.text = "Enter Master Password"
            changeButtonTheme(btnLog, resources.getColor(R.color.black), resources.getColor(R.color.white))
            changeButtonTheme(btnReg, resources.getColor(R.color.white), resources.getColor(R.color.black))

        }else if(whichFragment.equals("reg")){
            transaction.replace(R.id.container, registrationFragment)
            btnSetPass.text = "Set Master Password"
            changeButtonTheme(btnLog, resources.getColor(R.color.white), resources.getColor(R.color.black))
            changeButtonTheme(btnReg, resources.getColor(R.color.black), resources.getColor(R.color.white))
        }

        transaction.addToBackStack(null) // Add the transaction to the back stack if needed
        transaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }



}