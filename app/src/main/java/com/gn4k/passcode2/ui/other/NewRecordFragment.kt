package com.gn4k.passcode2.ui.other

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gn4k.passcode2.R
import com.google.android.material.slider.Slider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import java.util.Base64
import kotlin.random.Random

class NewRecordFragment : Fragment() {


    lateinit var view1 : View

    lateinit var tvLetterCount: TextView
    lateinit var isLowerCase: CheckBox
    lateinit var isNumber: CheckBox
    lateinit var isSymbols: CheckBox
    lateinit var isUpperCase: CheckBox
    lateinit var spinnerCategory: Spinner
    lateinit var spinnerName: Spinner
    lateinit var edId: EditText
    lateinit var edPassword: EditText
    lateinit var passStrongness: ProgressBar
    lateinit var btnGeneratePassword: Button
    lateinit var btnSaveData: Button

    var nameList = mutableListOf<String>()

    val categoryList = mutableListOf<String>()


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

        spinnerCategory = view1.findViewById(R.id.spinnerCategory)

        spinnerName = view1.findViewById(R.id.spinnerName)

        edId = view1.findViewById(R.id.edId)

        edPassword = view1.findViewById(R.id.edPassword)

        passStrongness = view1.findViewById(R.id.passStrongness)

        btnGeneratePassword = view1.findViewById(R.id.btnGeneratePassword)

        btnSaveData = view1.findViewById(R.id.saveData)


        val slider = view1.findViewById<Slider>(R.id.numberPicker)
        tvLetterCount = view1.findViewById(R.id.tvLetterCount)

        slider.addOnChangeListener { slider, value, fromUser ->
            val count =  value.toInt()
            tvLetterCount.text = count.toString()
        }

        nameList = mutableListOf<String>().apply {
            val webAppItems = resources.getStringArray(R.array.web_app_items)
            addAll(webAppItems)
        }

        val adapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, nameList) }
        if (adapter != null) {
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerName.adapter = adapter

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


        spinnerCategory.onItemSelectedListener = object :  AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                // Handle the selected item if needed
                val selectedCategory = categoryList[position]
                Toast.makeText(context, "Selected: $selectedCategory", Toast.LENGTH_SHORT).show()
                if (selectedCategory.equals("Other")){
                    startCategoryDialog()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Do nothing here
            }
        }

        spinnerName.onItemSelectedListener = object :  AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                // Handle the selected item if needed
                val selectedName = spinnerName.selectedItem
                Toast.makeText(context, "Selected: $selectedName", Toast.LENGTH_SHORT).show()
                if (selectedName.equals("Other")){
                    startNameDialog()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Do nothing here
            }
        }

        btnSaveData.setOnClickListener { sendPassToDB() }

        getCategoryList()
        return view1
    }

    fun sendPassToDB() {

        if (!edId.text.toString().isEmpty() && !edPassword.text.toString().isEmpty()) {
            // Get a reference to the database
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("users").child(loadLocalData()).child("password").child(spinnerCategory.selectedItem.toString())
            val encodedPass = Base64.getEncoder().encodeToString(edPassword.text.toString().toByteArray())
            // Create a Map to represent your data
            val userData = mapOf(
                "name" to spinnerName.selectedItem.toString(),
                "userId" to edId.text.toString(),
                "password" to encodedPass
            )

            // Push the data to the database
            val key = myRef.push().key
            if (key != null) {
                myRef.child(key).setValue(userData)
            }

            Toast.makeText(context, "Password Saved Successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Fill all Fields", Toast.LENGTH_SHORT).show()
        }
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

    fun loadLocalData() : String{

        val sharedPreferences = context?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("userName", null).toString()
        return id
    }

    fun startCategoryDialog() {
        val dialog = context?.let { Dialog(it, R.style.AppBottomSheetDialogTheme) }
        dialog?.setContentView(R.layout.add_new_category)

        val save : Button? = dialog?.findViewById(R.id.sendBtn);
        val edCategory : EditText? = dialog?.findViewById(R.id.edCategory);
        val cancel : Button? = dialog?.findViewById(R.id.cancelBtn);

        if (save != null) {
            save.setOnClickListener {
                val category = edCategory?.text.toString()
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("users").child(loadLocalData()).child("password")

                // Create a Map to represent your data
                val userData = mapOf(
                    category to "null"
                )

                // Push the data to the database
                myRef.updateChildren(userData)
                categoryList.add(category)
                spinnerCategory.setSelection(categoryList.size-1)
                dialog.cancel()
            }
        }

        if (cancel != null) {
            cancel.setOnClickListener { dialog.cancel() }
        }

        dialog?.setCancelable(true)
        dialog?.show()
    }


    fun startNameDialog() {
        val dialog = context?.let { Dialog(it, R.style.AppBottomSheetDialogTheme) }
        dialog?.setContentView(R.layout.add_new_category)

        val title : TextView? = dialog?.findViewById(R.id.text);
        val save : Button? = dialog?.findViewById(R.id.sendBtn);
        val edName : EditText? = dialog?.findViewById(R.id.edCategory);
        val cancel : Button? = dialog?.findViewById(R.id.cancelBtn);

        if (title != null) {
            title.text = "New Name"
        }

        if (edName != null) {
            edName.hint = "New Name"
        }

        if (save != null) {
            save.setOnClickListener {
                val category = edName?.text.toString()
                if (category.isNotEmpty()) {
                    // Add the new item to the adapter
                    (spinnerName.adapter as ArrayAdapter<String>).add(category)
                    // Notify the adapter that the data set has changed
                    (spinnerName.adapter as ArrayAdapter<String>).notifyDataSetChanged()

                    // Optionally, set the Spinner to the new item
                    val position = (spinnerName.adapter as ArrayAdapter<String>).getPosition(category)
                    spinnerName.setSelection(position)

                    // Clear the EditText for the next input
                }
//                spinnerCategory.setSelection(categoryList.size-1)
                dialog.cancel()
            }
        }

        if (cancel != null) {
            cancel.setOnClickListener { dialog.cancel() }
        }

        dialog?.setCancelable(true)
        dialog?.show()
    }

    private fun getCategoryList() {
        val databaseReference = FirebaseDatabase.getInstance().reference

        // Reference to the specific node you want to retrieve children from
        val userNodeReference = databaseReference.child("users").child(loadLocalData())


        userNodeReference.child("password").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Handle data change
                val passwordMapType = object : GenericTypeIndicator<Map<String, Any>>() {}
                val passwordMap = snapshot.getValue(passwordMapType)
                if (passwordMap != null) {

                    // Iterate through the top-level children
                    for (categorySnapshot in snapshot.children) {
                        val categoryKey = categorySnapshot.key
                        categoryList.add(categoryKey ?: "")
                    }

                    categoryList.add("Other")
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryList)

                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    // Apply the adapter to the spinner
                    spinnerCategory.adapter = adapter
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
    }


    companion object{

    }
}