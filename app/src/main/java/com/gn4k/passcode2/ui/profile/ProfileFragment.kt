package com.gn4k.passcode2.ui.profile

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.gn4k.passcode2.R
import com.gn4k.passcode2.ui.home.Home
import com.gn4k.passcode2.ui.onboard.Onboard
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment() {

    lateinit var view1 : View
    private lateinit var tvName: TextView
    lateinit var pro: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_profile, container, false)
        pro = view1.findViewById(R.id.pro)

        tvName = view1.findViewById(R.id.tvName)
        val tvEmail: TextView = view1.findViewById(R.id.tvEmail)

        val btnEditProfile: TextView = view1.findViewById(R.id.btnEditProfile)
        val btnSecurity: TextView = view1.findViewById(R.id.btnSecurity)
        val btnTrustedDevices: TextView = view1.findViewById(R.id.btnTrustedDevices)
        val btnRecycleBin: TextView = view1.findViewById(R.id.btnRecycleBin)


        val btnLogout: TextView = view1.findViewById(R.id.btnLogout)

        tvName.text = Home.name
        tvEmail.text = Home.email

        btnEditProfile.setOnClickListener { editNameDialog() }

        btnSecurity.setOnClickListener {
            val intent = Intent(activity, Security::class.java)
            startActivity(intent)
        }

        btnTrustedDevices.setOnClickListener {
            val intent = Intent(activity, TrustedDevices::class.java)
            startActivity(intent)
        }

        btnRecycleBin.setOnClickListener {
            val intent = Intent(activity, RecyclerBin::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            logout()
        }

        return view1
    }


    private fun editNameDialog() {
        val dialog = context?.let { Dialog(it, R.style.AppBottomSheetDialogTheme) }
        dialog?.setContentView(R.layout.change_name_popup)

        val btnSave : Button? = dialog?.findViewById(R.id.sendBtn)
        val btnCancel : Button? = dialog?.findViewById(R.id.cancelBtn)

        val edFirstName : EditText? = dialog?.findViewById(R.id.edFirstName)
        val edLastName : EditText? = dialog?.findViewById(R.id.edLastName)


        btnSave?.setOnClickListener {

            pro.visibility = View.VISIBLE
            val database = FirebaseDatabase.getInstance().reference

            val firstName = edFirstName?.text.toString()
            val lastName = edLastName?.text.toString()
            dialog.cancel()

            // Create a map only for the fields you want to update
            val dataMap = hashMapOf<String, Any>()
            if (firstName.isNotEmpty()) {
                dataMap["firstName"] = firstName
            }
            if (lastName.isNotEmpty()) {
                dataMap["lastName"] = lastName
            }

            val userId = loadLocalData()
            val userDetailsPath = "users/$userId/details"

            // Update only the specific fields
            database.child(userDetailsPath).updateChildren(dataMap)
                .addOnSuccessListener {
                    Home.name = "$firstName $lastName"
                    tvName.text = Home.name
                    Toast.makeText(context, "Account details updated", Toast.LENGTH_SHORT).show()
                    pro.visibility = View.GONE

                }
                .addOnFailureListener {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                    pro.visibility = View.GONE

                }
        }


        btnCancel?.setOnClickListener { dialog.cancel() }

        dialog?.setCancelable(true)
        dialog?.show()
    }

    fun loadLocalData(): String {
        val sharedPreferences = context?.getSharedPreferences("login", Context.MODE_PRIVATE)
        return sharedPreferences?.getString("userName", null).toString()
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