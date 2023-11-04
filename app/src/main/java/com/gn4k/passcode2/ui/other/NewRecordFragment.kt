package com.gn4k.passcode2.ui.other

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gn4k.passcode2.R
import com.google.android.material.slider.Slider

class NewRecordFragment : Fragment() {


    lateinit var view1 : View

    lateinit var tvLetterCount: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_new_record, container, false)

        val slider = view1.findViewById<Slider>(R.id.numberPicker)
        tvLetterCount = view1.findViewById(R.id.tvLetterCount)

        slider.addOnChangeListener { slider, value, fromUser ->
            val count =  value.toInt()
            tvLetterCount.text = count.toString()
        }

        return view1
    }

    companion object {

    }
}