package com.gn4k.passcode2.ui.home.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.gn4k.passcode2.R
import com.gn4k.passcode2.ui.other.Other


class SettingsFragment : Fragment() {


    lateinit var view1: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_settings, container, false)
        val btnProfile = view1.findViewById<TextView>(R.id.btnProfile)
        val btnPremium = view1.findViewById<TextView>(R.id.btnPremium)
        val btnAbout = view1.findViewById<TextView>(R.id.btnAbout)
        val btnHelp = view1.findViewById<TextView>(R.id.btnHelp)
        val tvVersionName = view1.findViewById<TextView>(R.id.tvVersionName)


        val pInfo = requireContext().packageManager.getPackageInfo(
            requireContext().packageName, 0
        )
        val version = pInfo.versionName
        tvVersionName.text = version

        btnAbout.setOnClickListener {
            val intent = Intent(activity, Other::class.java )
            intent.putExtra("key", "About")
            startActivity( intent )
        }

        btnHelp.setOnClickListener {
            val intent = Intent(activity, Other::class.java )
            intent.putExtra("key", "Help")
            startActivity( intent )
        }

        btnProfile.setOnClickListener {
            val intent = Intent(activity, Other::class.java )
            intent.putExtra("key", "Profile")
            startActivity( intent )
        }

        btnPremium.setOnClickListener {
            val intent = Intent(activity, Premium::class.java )
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