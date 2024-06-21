package com.gn4k.passcode2.adapter

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gn4k.passcode2.R
import com.gn4k.passcode2.data.BrandData
import com.gn4k.passcode2.data.PassData
import com.gn4k.passcode2.ui.home.Home
import com.gn4k.passcode2.ui.other.Other
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import java.util.Base64

class SearchAdapter(
    private var categoryList: List<String>,
    private var keyList: List<String>,
    private var passDataList: List<PassData>,
    private val context: Context
) : RecyclerView.Adapter<SearchAdapter.PassDataViewHolder>() {

    private var filteredPassDataList: List<PassData> = passDataList

    class PassDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.tvname)
        val tvId: TextView = itemView.findViewById(R.id.tvId)
        val item: LinearLayout = itemView.findViewById(R.id.item)
        val btnCopyPass: ImageView = itemView.findViewById(R.id.btnCopyPass)
        val imgLogo: ImageView = itemView.findViewById(R.id.imgLogo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_password, parent, false)
        return PassDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: PassDataViewHolder, position: Int) {
        val passData = filteredPassDataList[position]
        val brandData = BrandData()
        holder.textViewName.text = passData.name
        holder.tvId.text = passData.userId
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
            intent.putExtra("category", categoryList[position])
            intent.putExtra("logoIndex", passData.logoIndex)
            context.startActivity(intent)
        }

        holder.btnCopyPass.setOnClickListener {

            Home.rewardedAd?.let { ad ->
                ad.show(context as Activity, OnUserEarnedRewardListener { rewardItem ->
                    // Handle the reward.
                    val rewardAmount = rewardItem.amount
                    val rewardType = rewardItem.type
                    Log.d(ContentValues.TAG, "User earned the reward.")
                })

                Home.rewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                    override fun onAdClicked() {
                        // Called when a click is recorded for an ad.
                        Log.d(ContentValues.TAG, "Ad was clicked.")
                    }

                    override fun onAdDismissedFullScreenContent() {
                        val clipboardManager: ClipboardManager =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clipData = ClipData.newPlainText("Copied Text", decodedPass)
                        clipboardManager.setPrimaryClip(clipData)
                        loadReward() 
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        // Called when ad fails to show.
                        Home.rewardedAd = null
                    }

                    override fun onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        Log.d(ContentValues.TAG, "Ad recorded an impression.")
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d(ContentValues.TAG, "Ad showed fullscreen content.")
                    }
                }

            } ?: run {
                val clipboardManager: ClipboardManager =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("Copied Text", decodedPass)
                clipboardManager.setPrimaryClip(clipData)
            }


        }
    }

    fun loadReward() {
        var adRequest = AdRequest.Builder().build()
        RewardedAd.load(context, context.getString(R.string.full_screen_ad_key) + Home.stopAd, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Home.rewardedAd = null
            }

            override fun onAdLoaded(ad: RewardedAd) {
                Home.rewardedAd = ad
            }
        })
    }

    override fun getItemCount(): Int {
        return filteredPassDataList.size
    }

    fun filter(query: String) {
        filteredPassDataList = if (query.isEmpty()) {
            passDataList
        } else {
            passDataList.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.userId.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}
