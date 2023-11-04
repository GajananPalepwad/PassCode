package com.gn4k.passcode2.ui.reg_and_login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.gn4k.passcode2.R


class Login : Fragment() {

    lateinit var view1: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_login, container, false)

        edEmail = view1.findViewById(R.id.edEmail)

        return view1
    }

    companion object{
        lateinit var edEmail: EditText
    }
}