package com.gn4k.passcode2.ui.home

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.window.OnBackInvokedDispatcher
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.gn4k.passcode2.R
import com.gn4k.passcode2.data.PassData
import com.gn4k.passcode2.ui.other.NewRecordFragment
import com.gn4k.passcode2.ui.other.Other
import com.gn4k.passcode2.ui.profile.ProfileFragment

class Home : AppCompatActivity() {

    lateinit var navBar : View
    lateinit var navText : TextView
    lateinit var home : TextView
    lateinit var analysis : TextView
    lateinit var search : TextView
    lateinit var settings : TextView
    lateinit var btnProfile : ImageView
    lateinit var btnNewRecord : ImageView


    var transaction = supportFragmentManager.beginTransaction()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navBar = findViewById(R.id.navBar)

        home = navBar.findViewById(R.id.home)
        analysis = navBar.findViewById(R.id.analysis)
        search = navBar.findViewById(R.id.search)
        settings = navBar.findViewById(R.id.settings)

        navText = findViewById(R.id.navText)
        btnProfile = findViewById(R.id.btnProfile)
        btnNewRecord = findViewById(R.id.btnNewRecord)

        setFragment(HomeFragment())

        home.setOnClickListener {
            navAnim(home, R.drawable.fill_home_ic)
            setFragment(HomeFragment())
            navText.text = "Passwords"
        }

        analysis.setOnClickListener {
            navAnim(analysis, R.drawable.filled_shield_ic)
            setFragment(AnalysisFragment())
            navText.text = "Security"
        }

        search.setOnClickListener {
            navAnim(search, R.drawable.filled_search_ic)
            setFragment(SearchFragment())
            navText.text = "Search"
        }

        settings.setOnClickListener {
            navAnim(settings, R.drawable.filled_settings_ic)
            setFragment(SettingsFragment())
            navText.text = "Settings"
        }

        btnProfile.setOnClickListener {
            val intent = Intent(applicationContext, Other::class.java )
            intent.putExtra("key", "Profile")
            startActivity( intent )
        }

        btnNewRecord.setOnClickListener {
            val intent = Intent(applicationContext, Other::class.java)
            intent.putExtra("key", "New")
            startActivity(intent)
        }

    }


    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    private fun navAnim(view1 : TextView, id : Int){
        var drawable = resources.getDrawable(R.drawable.empty_home_ic)
        home.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)

        drawable = resources.getDrawable(R.drawable.empty_shield_search_ic)
        analysis.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)

        drawable = resources.getDrawable(R.drawable.empty_search_ic)
        search.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)

        drawable = resources.getDrawable(R.drawable.empty_settings_ic)
        settings.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)

        drawable = resources.getDrawable(id)
        view1.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)

    }

    private fun setFragment(fragment: Fragment){
        transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null) // Add the transaction to the back stack if needed
        transaction.commit()
    }

    companion object {
        val allPassList: MutableList<PassData> = mutableListOf()
        val allPassKey: MutableList<String> = mutableListOf()
        val allCategory: MutableList<String> = mutableListOf()
        var name: String = ""
        var email: String = ""

    }

}