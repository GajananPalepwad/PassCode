package com.gn4k.passcode2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gn4k.passcode2.R
import com.gn4k.passcode2.data.BrandData
import com.gn4k.passcode2.data.PassData
import com.gn4k.passcode2.ui.home.settings.Premium
import com.gn4k.passcode2.ui.home.Home
import com.gn4k.passcode2.ui.profile.RecyclerBin
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Base64


class RecyclerBinPassDataAdapter(private val category: String, private val keyList: List<String>, private val passDataList: List<PassData>, private val context: Context) :
    RecyclerView.Adapter<RecyclerBinPassDataAdapter.PassDataViewHolder>() {

    class PassDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.tvname)
        val textViewId: TextView = itemView.findViewById(R.id.tvId)
        val item: LinearLayout = itemView.findViewById(R.id.item)
        val btnRestorPass: ImageView = itemView.findViewById(R.id.btnDeletePass)
        val imgLogo: ImageView = itemView.findViewById(R.id.imgLogo)
        // Add more TextViews for other properties if needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycle_bin_password, parent, false)
        return PassDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: PassDataViewHolder, position: Int) {
        val passData = passDataList[position]
        val brandData = BrandData()
        holder.textViewName.text = passData.name
        holder.textViewId.text = passData.userId
        holder.imgLogo.setImageResource(brandData.logoArray[passData.logoIndex.toInt()]);

        val decodedBytes = Base64.getDecoder().decode(passData.password)
        val decodedPass = String(decodedBytes, Charsets.UTF_8)


        holder.btnRestorPass.setOnClickListener {
            RecyclerBin.pro.visibility = View.VISIBLE
            if(Home.isSubscribe) {
                val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                val reference: DatabaseReference = database.reference
                    .child("users")
                    .child(loadLocalData())
                    .child("deletedPassword")
                    .child(category)
                    .child(keyList[position])
                reference.removeValue()
                sendPassToDB(
                    category,
                    decodedPass,
                    passData.name,
                    passData.logoIndex,
                    passData.userId
                )
            }else{
                val intent = Intent(context, Premium::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
                RecyclerBin.pro.visibility = View.GONE
            }
        }

    }

    private fun sendPassToDB(category: String, pass: String, name: String, logoIndex: String, useId: String, ) {

        // Get a reference to the database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("users").child(loadLocalData()).child("password").child(category)
        val encodedPass = Base64.getEncoder().encodeToString(pass.toByteArray())
        // Create a Map to represent your data
        val userData = mapOf(
            "name" to name,
            "logoIndex" to logoIndex,
            "userId" to useId,
            "password" to encodedPass
        )

        // Push the data to the database
        val key = myRef.push().key
        if (key != null) {
            myRef.child(key).setValue(userData).addOnSuccessListener {
                Toast.makeText(context, "Password Recovered Successfully", Toast.LENGTH_SHORT).show()
                RecyclerBin.pro.visibility = View.GONE
            }.addOnFailureListener{
                Toast.makeText(context, "Something went WRONG!", Toast.LENGTH_SHORT).show()
                RecyclerBin.pro.visibility = View.GONE
            }
        }


    }

    fun loadLocalData() : String{
        val sharedPreferences = context?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("userName", null).toString()
        return id
    }


    override fun getItemCount(): Int {
        return passDataList.size
    }
}
