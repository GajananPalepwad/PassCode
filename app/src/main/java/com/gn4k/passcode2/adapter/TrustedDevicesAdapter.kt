package com.gn4k.passcode2.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gn4k.passcode2.R
import com.gn4k.passcode2.data.DeviceLoginDetail
import com.gn4k.passcode2.ui.home.Home
import com.gn4k.passcode2.ui.other.Other
import com.gn4k.passcode2.ui.profile.TrustedDevices
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TrustedDevicesAdapter(private val loginDetails: MutableList<DeviceLoginDetail>, val context: Context) : RecyclerView.Adapter<TrustedDevicesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_device_login, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val detail = loginDetails[position]

        holder.item.setOnClickListener { showYesNoDialog(loadLocalData(), detail.deviceNameModel, holder.itemView, position) }

        holder.deviceTextView.text = detail.deviceNameModel

        if(detail.deviceNameModel == Home.deviceName){
            holder.tvThis.visibility = View.VISIBLE
            holder.item.setOnClickListener {}
        }
        holder.timeTextView.text = timeFormat(detail.loginTime)


    }

    private fun timeFormat(timeDate: String): String {
        val originalFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val originalDateTime = LocalDateTime.parse(timeDate, originalFormatter)
        val desiredFormatter = DateTimeFormatter.ofPattern("'At' HH:mm 'on' dd MMM yyyy")
        return originalDateTime.format(desiredFormatter)
    }

    private fun showYesNoDialog(id: String, deviceName: String, itemView: View, position: Int) {
        val builder = AlertDialog.Builder(itemView.context)
        builder.setTitle("Warning!!!")
            .setMessage("Do you want to block $deviceName?")
            .setPositiveButton("Yes") { dialog, which ->

                TrustedDevices.pro.visibility = View.VISIBLE
                val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                val reference: DatabaseReference = database.reference
                    .child("users")
                    .child(loadLocalData())
                    .child("loginDetails")
                    .child("loggedInDevices")
                    .child(deviceName)

                reference.removeValue()
                loginDetails.removeAt(position)
                notifyItemRemoved(position)
                blockDevice(id, deviceName)

                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }


    private fun blockDevice(id: String, deviceName: String){
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("users").child(id).child("loginDetails").child("blockedDevice").child(deviceName)
        myRef.setValue(formattedDateTime)
        TrustedDevices.pro.visibility = View.GONE
    }

    fun loadLocalData() : String{
        val sharedPreferences = context?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("userName", null).toString()
        return id
    }

    override fun getItemCount(): Int {
        return loginDetails.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceTextView: TextView = itemView.findViewById(R.id.tvDevice)
        val timeTextView: TextView = itemView.findViewById(R.id.tvTime)
        val tvThis: TextView = itemView.findViewById(R.id.tvThis)
        val item: LinearLayout = itemView.findViewById(R.id.item)

    }
}
