package com.gn4k.passcode2.ui.other

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.set
import com.gn4k.passcode2.R
import com.google.android.material.slider.Slider
import kotlin.random.Random
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getColorStateList

class NewRecordFragment : Fragment() {


    lateinit var view1 : View

    lateinit var tvLetterCount: TextView
    lateinit var isLowerCase: CheckBox
    lateinit var isNumber: CheckBox
    lateinit var isSymbols: CheckBox
    lateinit var isUpperCase: CheckBox

    lateinit var edPassword: EditText

    lateinit var passStrongness: ProgressBar

    lateinit var btnGeneratePassword: Button

    var LowerCase = false
    var Number = false
    var Symbols = false
    var UpperCase = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_new_record, container, false)

        isLowerCase = view1.findViewById(R.id.isLowerCase)
        isNumber = view1.findViewById(R.id.isNumber)
        isSymbols = view1.findViewById(R.id.isSymbols)
        isUpperCase = view1.findViewById(R.id.isUpperCase)

        edPassword = view1.findViewById(R.id.edPassword)

        passStrongness = view1.findViewById(R.id.passStrongness)

        btnGeneratePassword = view1.findViewById(R.id.btnGeneratePassword)


        val slider = view1.findViewById<Slider>(R.id.numberPicker)
        tvLetterCount = view1.findViewById(R.id.tvLetterCount)

        slider.addOnChangeListener { slider, value, fromUser ->
            val count =  value.toInt()
            tvLetterCount.text = count.toString()
        }

        btnGeneratePassword.setOnClickListener {

            Number = isNumber.isChecked
            Symbols = isSymbols.isChecked
            LowerCase = isLowerCase.isChecked
            UpperCase = isUpperCase.isChecked
            var count = tvLetterCount.text.toString()

            var generatedPass = ""

            if (Number || Symbols || LowerCase || UpperCase) {
                generatedPass = generatePassword(
                    count.toInt(),
                    includeNumbers = Number,
                    includeSymbols = Symbols,
                    includeLowercase = LowerCase,
                    includeUppercase = UpperCase
                )
                calculatePasswordStrength(generatedPass)
            } else{
                Toast.makeText(context, "Atleast choose one category", Toast.LENGTH_SHORT).show()
            }

            var passStrongNumber = calculatePasswordStrength(generatedPass)

            passStrongness.progress = passStrongNumber

            var hexBackgroundColor = "#1CFF00"

            when {
                passStrongNumber >= 66 -> hexBackgroundColor = "#1CFF00"
                passStrongNumber in 33 until 66 -> hexBackgroundColor = "#FFFB00"
                passStrongNumber < 33 -> hexBackgroundColor = "#FF0000"
            }

            passStrongness.progressTintList = ColorStateList.valueOf(Color.parseColor(hexBackgroundColor))

            generatedPass?.let{
                edPassword.setText(it)
            }

        }

        return view1
    }

    fun generatePassword(
        length: Int,
        includeNumbers: Boolean,
        includeSymbols: Boolean,
        includeLowercase: Boolean,
        includeUppercase: Boolean
    ): String {
        val lowercaseChars = "abcdefghijklmnopqrstuvwxyz"
        val uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val numberChars = "0123456789"
        val symbolChars = "!@#$%^&*()-_+=<>?/{}[]|"

        val allChars = buildString {
            if (includeLowercase) append(lowercaseChars)
            if (includeUppercase) append(uppercaseChars)
            if (includeNumbers) append(numberChars)
            if (includeSymbols) append(symbolChars)
        }

        require(allChars.isNotEmpty()) { "At least one character set must be included." }

        return buildString {
            repeat(length) {
                append(allChars[Random.nextInt(allChars.length)])
            }
        }
    }

    fun calculatePasswordStrength(password: String): Int {
        val lengthScore = password.length // Maximum 10 points for length
        val lowercaseScore = if (password.any { it.isLowerCase() }) 17 else 0
        val uppercaseScore = if (password.any { it.isUpperCase() }) 17 else 0
        val digitScore = if (password.any { it.isDigit() }) 17 else 0
        val symbolScore = if (password.any { !it.isLetterOrDigit() }) 17 else 0

        return lengthScore + lowercaseScore + uppercaseScore + digitScore + symbolScore
    }
    companion object{

    }
}