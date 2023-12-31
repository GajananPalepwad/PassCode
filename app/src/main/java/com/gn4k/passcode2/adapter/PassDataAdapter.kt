package com.gn4k.passcode2.adapter

import android.R.attr.label
import android.R.attr.text
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.gn4k.passcode2.R
import com.gn4k.passcode2.data.BrandData
import com.gn4k.passcode2.data.PassData
import com.gn4k.passcode2.ui.other.Other
import java.util.Base64


class PassDataAdapter(private val category: String, private val keyList: List<String>, private val passDataList: List<PassData>, private val context: Context) :
    RecyclerView.Adapter<PassDataAdapter.PassDataViewHolder>() {

    class PassDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.tvname)
        val textViewId: TextView = itemView.findViewById(R.id.tvId)
        val item: LinearLayout = itemView.findViewById(R.id.item)
        val btnCopyPass: ImageView = itemView.findViewById(R.id.btnCopyPass)
        val imgLogo: ImageView = itemView.findViewById(R.id.imgLogo)
        // Add more TextViews for other properties if needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_password, parent, false)
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

        holder.item.setOnClickListener {

            val intent = Intent(context, Other::class.java)
            intent.putExtra("key", "PassDetails")
            intent.putExtra("name", passData.name)
            intent.putExtra("pass", decodedPass)
            intent.putExtra("id", passData.userId)
            intent.putExtra("password_child_key", keyList[position])
            intent.putExtra("category", category)
            intent.putExtra("logoIndex", passData.logoIndex)

            context.startActivity(intent)
        }

        holder.btnCopyPass.setOnClickListener {
            val clipboardManager: ClipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Copied Text", decodedPass)
            clipboardManager.setPrimaryClip(clipData)

        }

    }

    override fun getItemCount(): Int {
        return passDataList.size
    }
}
