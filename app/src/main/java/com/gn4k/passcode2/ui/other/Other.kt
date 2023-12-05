package com.gn4k.passcode2.ui.other

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.gn4k.passcode2.R
import com.gn4k.passcode2.ui.profile.ProfileFragment

class Other : AppCompatActivity() {

    var transaction = supportFragmentManager.beginTransaction()

    lateinit var tvHeader : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)

        val btnBack = findViewById<ImageView>(R.id.btnBack)

        val whichFragment = intent.getStringExtra("key").toString()

        tvHeader = findViewById(R.id.tvHeader)

        btnBack.setOnClickListener {onBackPressed()}

        if(whichFragment.equals("New")){
            setFragment(NewRecordFragment(), "New record")
        } else if (whichFragment.equals("About")){
            setFragment(AboutFragment(), "About")
        } else if (whichFragment.equals("Profile")){
            setFragment(ProfileFragment(), "Profile")
        } else if (whichFragment.equals("PassDetails")){
            detailName = intent.getStringExtra("name").toString()
            detailId = intent.getStringExtra("id").toString()
            detailPass = intent.getStringExtra("pass").toString()
            detailKey = intent.getStringExtra("password_child_key").toString()
            detailCategory = intent.getStringExtra("category").toString()
            setFragment(PasswordDetailsFragment(), "Back")
        }


    }

    private fun setFragment(fragment: Fragment, header : String){
        transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.container, fragment)

        transaction.addToBackStack(null) // Add the transaction to the back stack if needed
        transaction.commit()

        tvHeader.text = header
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish() // This will close the current activity and return to the previous one.
    }

    companion object{
        lateinit var detailName: String
        lateinit var detailId: String
        lateinit var detailPass: String
        lateinit var detailKey: String
        lateinit var detailCategory: String

    }


}