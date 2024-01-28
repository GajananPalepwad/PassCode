package com.gn4k.passcode2.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gn4k.passcode2.R
import com.gn4k.passcode2.ui.home.Home
import com.gn4k.passcode2.ui.onboard.Onboard
import com.gn4k.passcode2.ui.other.RecyclerBin
import com.gn4k.passcode2.ui.reg_and_login.password.Password

class ProfileFragment : Fragment() {

    lateinit var view1 : View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_profile, container, false)

        val tvName: TextView = view1.findViewById(R.id.tvName)
        val tvEmail: TextView = view1.findViewById(R.id.tvEmail)

        val btnRecycleBin: TextView = view1.findViewById(R.id.btnRecycleBin)


        val btnLogout: TextView = view1.findViewById(R.id.btnLogout)

        tvName.text = Home.name
        tvEmail.text = Home.email

        btnRecycleBin.setOnClickListener {
            val intent = Intent(activity, RecyclerBin::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            logout()
        }

        return view1
    }

    private fun logout(){
        val sharedPreferences = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        editor?.apply{
            putString("userName", "")
        }?.apply()

        val intent = Intent(activity, Onboard::class.java)
        startActivity(intent)
        activity?.finish()
    }

    companion object {

    }
}