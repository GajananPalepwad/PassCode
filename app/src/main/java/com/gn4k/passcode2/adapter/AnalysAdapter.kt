package com.gn4k.passcode2.adapter

import android.R.attr.label
import android.R.attr.text
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.gn4k.passcode2.R
import com.gn4k.passcode2.data.BrandData
import com.gn4k.passcode2.data.PassData
import com.gn4k.passcode2.ui.home.AnalysisFragment
import com.gn4k.passcode2.ui.other.Other
import java.util.Base64


class AnalysAdapter(private val categoryList: List<String>, private val keyList: List<String>, private val passDataList: List<PassData>, private val context: Context) :
    RecyclerView.Adapter<AnalysAdapter.PassDataViewHolder>() {

    class PassDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.tvname)
        val tvPassword: TextView = itemView.findViewById(R.id.tvPassword)
        val item: LinearLayout = itemView.findViewById(R.id.item)
        val btnNext: ImageView = itemView.findViewById(R.id.btnNext)
        val imgLogo: ImageView = itemView.findViewById(R.id.imgLogo)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        var passStrongness: ProgressBar = itemView.findViewById(R.id.passStrongness)
        // Add more TextViews for other properties if needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_security, parent, false)
        return PassDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: PassDataViewHolder, position: Int) {
        val passData = passDataList[position]
        val brandData = BrandData()
        holder.textViewName.text = passData.name
        holder.imgLogo.setImageResource(brandData.logoArray[passData.logoIndex.toInt()]);

        val decodedBytes = Base64.getDecoder().decode(passData.password)
        val decodedPass = String(decodedBytes, Charsets.UTF_8)

        holder.tvPassword.text = decodedPass


        val strongNess = calculatePasswordStrength(decodedPass)

        holder.passStrongness.progress = strongNess

        if(strongNess> -1 && strongNess < 34){
            holder.tvStatus.text = "Risk"
            AnalysisFragment.risk += 1
            AnalysisFragment.tvRisk.text = "${AnalysisFragment.risk}"
        }else if(strongNess > 33 && strongNess < 67){
            holder.tvStatus.text = "Weak"
            AnalysisFragment.weak += 1
            AnalysisFragment.tvWeak.text = "${AnalysisFragment.weak}"
        }else if(strongNess > 66 && strongNess < 101){
            holder.tvStatus.text = "Safe"
            AnalysisFragment.safe += 1
            AnalysisFragment.tvSafe.text = "${AnalysisFragment.safe}"
        }

        var total = AnalysisFragment.risk + AnalysisFragment.weak + AnalysisFragment.safe
        var secured = 0

        var div = AnalysisFragment.safe.toDouble() / total

        if(total != 0) {
            secured = (div * 100).toInt()
        }

        AnalysisFragment.tvSecuredNumber.text = "$secured" + "%"
        AnalysisFragment.progressSecured.progress = secured
        AnalysisFragment.tvSecured.text = "$secured" + "% secured"


        var hexBackgroundColor = "#1CFF00"
        when {
            strongNess >= 66 -> hexBackgroundColor = "#1CFF00"
            strongNess in 33 until 66 -> hexBackgroundColor = "#FFFB00"
            strongNess < 33 -> hexBackgroundColor = "#FF0000"
        }
        holder.passStrongness.progressTintList = ColorStateList.valueOf(Color.parseColor(hexBackgroundColor))


        holder.btnNext.setOnClickListener {

            val intent = Intent(context, Other::class.java)
            intent.putExtra("key", "PassDetails")
            intent.putExtra("name", passData.name)
            intent.putExtra("pass", decodedPass)
            intent.putExtra("id", passData.userId)
            intent.putExtra("password_child_key", keyList[position])
            intent.putExtra("category", categoryList[position])
            intent.putExtra("logoIndex", passData.logoIndex)

            context.startActivity(intent)
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

    override fun getItemCount(): Int {
        return passDataList.size
    }
}
