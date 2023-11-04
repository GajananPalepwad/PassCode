package com.gn4k.passcode2.ui.reg_and_login.password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chaos.view.PinView
import com.gn4k.passcode2.R


class SetPassword : Fragment() {

    lateinit var view1 : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_set_password, container, false)

        pass = view1.findViewById(R.id.pass)
        confoPass = view1.findViewById(R.id.confo_pass)


        return view1
    }

    companion object{

        lateinit var pass : PinView
        lateinit var confoPass : PinView

    }

}