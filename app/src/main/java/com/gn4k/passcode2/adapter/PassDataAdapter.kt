package com.gn4k.passcode2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gn4k.passcode2.R
import com.gn4k.passcode2.data.PassData

class PassDataAdapter(private val passDataList: List<PassData>) :
    RecyclerView.Adapter<PassDataAdapter.PassDataViewHolder>() {

    class PassDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.tvname)
        val textViewId: TextView = itemView.findViewById(R.id.tvId)
        // Add more TextViews for other properties if needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_password, parent, false)
        return PassDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: PassDataViewHolder, position: Int) {
        val passData = passDataList[position]
        holder.textViewName.text = passData.name
        holder.textViewId.text = passData.userId
        // Bind other properties if needed

    }

    override fun getItemCount(): Int {
        return passDataList.size
    }
}
