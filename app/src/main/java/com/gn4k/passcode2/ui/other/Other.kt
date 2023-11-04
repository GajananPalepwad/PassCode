package com.gn4k.passcode2.ui.other

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.gn4k.passcode2.R

class Other : AppCompatActivity() {

    var transaction = supportFragmentManager.beginTransaction()

    lateinit var tvHeader : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)

        val btnBack = findViewById<ImageView>(R.id.btnBack)

        tvHeader = findViewById(R.id.tvHeader)

        btnBack.setOnClickListener {onBackPressed()}

        setFragment(NewRecordFragment(), "New record")
    }

    private fun setFragment(fragment: Fragment, header : String){
        transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.container, fragment)

        transaction.addToBackStack(null) // Add the transaction to the back stack if needed
        transaction.commit()

        tvHeader.text = header
    }

}