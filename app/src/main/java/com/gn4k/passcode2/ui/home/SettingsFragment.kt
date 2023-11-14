package com.gn4k.passcode2.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.gn4k.passcode2.R
import com.gn4k.passcode2.ui.other.Other
import com.gn4k.passcode2.ui.profile.ProfileFragment
import com.gn4k.passcode2.ui.reg_and_login.password.Password

class SettingsFragment : Fragment() {


    lateinit var view1: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_settings, container, false)
        var btnProfile = view1.findViewById<TextView>(R.id.btnProfile)
        var btnAbout = view1.findViewById<TextView>(R.id.btnAbout)

        btnAbout.setOnClickListener {
            val intent = Intent(activity, Other::class.java )
            intent.putExtra("key", "About")
            startActivity( intent )
        }

        btnProfile.setOnClickListener {
            val intent = Intent(activity, Other::class.java )
            intent.putExtra("key", "Profile")
            startActivity( intent )
        }


        return view1
    }

//    private fun setFragment(fragment: Fragment){
//        var transaction = childFragmentManager.beginTransaction()
//
//        transaction.replace(view2.findViewById<ConstraintLayout>(R.id.container), fragment)
//
//        transaction.addToBackStack(null) // Add the transaction to the back stack if needed
//        transaction.commit()
//    }

    companion object {

    }
}