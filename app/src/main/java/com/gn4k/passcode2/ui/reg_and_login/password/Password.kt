package com.gn4k.passcode2.ui.reg_and_login.password

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.gn4k.passcode2.R
import com.gn4k.passcode2.data.User
import com.gn4k.passcode2.ui.home.Home
import com.gn4k.passcode2.ui.reg_and_login.RegistrationAndLogin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Base64

class Password : AppCompatActivity() {

    var transaction = supportFragmentManager.beginTransaction()
    lateinit var btnEnter: Button
    lateinit var btnUseAnotherAccount : Button
    lateinit var whichFragment: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        btnEnter = findViewById(R.id.btnSetPass)
        btnUseAnotherAccount = findViewById(R.id.btnUseAnotherAccount)
        whichFragment = intent.getStringExtra("key").toString()

        if (whichFragment.equals("saved")){
            btnUseAnotherAccount.visibility = View.VISIBLE
        }

        btnUseAnotherAccount.setOnClickListener {
            intent = Intent(applicationContext, RegistrationAndLogin::class.java)
            intent.putExtra("key", "reg");
            startActivity(intent)
        }


        btnEnter.setOnClickListener {
            if(whichFragment.equals("log")){

                val email = intent.getStringExtra("email").toString()
                val id = email.substringBefore("@")
                val password = EnterPassword.pass.text.toString()

                if(!password.equals("")) {
                    getPassForUser(id, password)
                }else{
                    Toast.makeText(applicationContext, "Please Enter the Password", Toast.LENGTH_SHORT).show()
                }



            }else if(whichFragment.equals("saved")) {

                val id = loadLocalData()
                val password = EnterPassword.pass.text.toString()
                if(!password.equals("")) {
                    getPassForUser(id, password)
                }else{
                    Toast.makeText(applicationContext, "Please Enter the Password", Toast.LENGTH_SHORT).show()
                }

            }else if(whichFragment.equals("reg")){

                val confopass = SetPassword.confoPass.text.toString()
                val pass = SetPassword.pass.text.toString()

                val firstName = intent.getStringExtra("firstName").toString()
                val lastName = intent.getStringExtra("lastName").toString()
                val email = intent.getStringExtra("email").toString()
                val id = email.substringBefore("@")

                if(!isAccountExist(id)) {

                    if (confopass.equals(pass)) {
                        Toast.makeText(applicationContext, "Successfully", Toast.LENGTH_SHORT)
                            .show()
                        val encodedPass = Base64.getEncoder().encodeToString(pass.toByteArray())
                        saveUserData(firstName, lastName, email, encodedPass, id)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Both Master password not matched",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Email Already Exist", Toast.LENGTH_SHORT).show()
                }

            }


        }
        setFragment()
    }

    private fun setFragment(){
        transaction = supportFragmentManager.beginTransaction()

        if(whichFragment.equals("log") || whichFragment.equals("saved")){
            transaction.replace(R.id.container, EnterPassword())
            btnEnter.text = "Enter Master Password"

        }else if(whichFragment.equals("reg")){
            transaction.replace(R.id.container, SetPassword())

        }

        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun saveUserData(firstName: String, lastName: String, email: String, encodedPass: String, id: String) {
        val database = FirebaseDatabase.getInstance()
        val usersRef: DatabaseReference = database.getReference("users").child(id).child("details")

        val user = User(firstName, lastName, email, encodedPass)

        // Push the user data to a unique key in the "users" node
        val userKey = usersRef.push()
        if (userKey != null) {
            usersRef.setValue(user)
            Toast.makeText(applicationContext, "Successful", Toast.LENGTH_SHORT).show()
            saveInLocal(id)
            saveCateogry(id)

            intent = Intent(applicationContext, Home::class.java)
            startActivity(intent)

        } else {
            Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    fun getPassForUser(id: String, password: String) {
        val database = FirebaseDatabase.getInstance()
        val userRef: DatabaseReference = database.getReference("users").child(id).child("details")


        // Attach a ValueEventListener to listen for changes in the user's data
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(User::class.java)
                    val encodedPass = user?.encodedPass
                    val decodedBytes = Base64.getDecoder().decode(encodedPass)
                    val decodedPass = String(decodedBytes, Charsets.UTF_8)

                    if(decodedPass.equals(password)){
                        saveInLocal(id)
                        Home.name = user?.firstName + " " + user?.lastName
                        Home.email = user?.email.toString()
                        intent = Intent(applicationContext, Home::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "Please Enter correct password", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(applicationContext, "Account not exist", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun isAccountExist(id: String): Boolean {
        val database = FirebaseDatabase.getInstance()
        val userRef: DatabaseReference = database.getReference("users").child(id).child("details")
        var returnVal = false

        // Attach a ValueEventListener to listen for changes in the user's data
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                returnVal = dataSnapshot.exists()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })

        return returnVal
    }

    fun saveInLocal(text : String){

        val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.apply{
            putString("userName", text)
        }.apply()

    }

    fun loadLocalData() : String{
        val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("userName", null).toString()
        return id
    }

    fun saveCateogry(id: String){
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val passwordRef: DatabaseReference = database.getReference("users").child(id)

// Save data under "work" node
        val workData = HashMap<String, Any>()
        workData["Priority"] = "null"
        workData["Work"] = "null"
        workData["Entertainment"] = "null"
        workData["Social"] = "null"
        passwordRef.child("password").setValue(workData)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    
}