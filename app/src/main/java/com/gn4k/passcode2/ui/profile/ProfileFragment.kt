package com.gn4k.passcode2.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gn4k.passcode2.R

class ProfileFragment : Fragment() {

    lateinit var view1 : View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_profile, container, false)



        return view1
    }

    companion object {

    }
}