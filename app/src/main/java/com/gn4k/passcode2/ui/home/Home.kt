package com.gn4k.passcode2.ui.home

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gn4k.passcode2.R
import com.gn4k.passcode2.data.PassData
import com.gn4k.passcode2.ui.home.settings.SettingsFragment
import com.gn4k.passcode2.ui.other.Other
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback


class Home : AppCompatActivity() {

    lateinit var navBar : View
    lateinit var navText : TextView
    lateinit var home : TextView
    lateinit var analysis : TextView
    lateinit var search : TextView
    lateinit var settings : TextView
    lateinit var btnProfile : ImageView
    lateinit var btnNewRecord : ImageView


    var transaction = supportFragmentManager.beginTransaction()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        MobileAds.initialize(this) { }
        loadReward()

        pro = findViewById(R.id.pro)

        navBar = findViewById(R.id.navBar)

        home = navBar.findViewById(R.id.home)
        analysis = navBar.findViewById(R.id.analysis)
        search = navBar.findViewById(R.id.search)
        settings = navBar.findViewById(R.id.settings)

        navText = findViewById(R.id.navText)
        btnProfile = findViewById(R.id.btnProfile)
        btnNewRecord = findViewById(R.id.btnNewRecord)

        setFragment(HomeFragment())

        home.setOnClickListener {
            navAnim(home, R.drawable.fill_home_ic)
            setFragment(HomeFragment())
            navText.text = "Passwords"
        }

        analysis.setOnClickListener {
            navAnim(analysis, R.drawable.filled_shield_ic)
            setFragment(AnalysisFragment())
            navText.text = "Security"
        }

        search.setOnClickListener {
            navAnim(search, R.drawable.filled_search_ic)
            setFragment(SearchFragment())
            navText.text = "Search"
        }

        settings.setOnClickListener {
            navAnim(settings, R.drawable.filled_settings_ic)
            setFragment(SettingsFragment())
            navText.text = "Settings"
        }

        btnProfile.setOnClickListener {
            val intent = Intent(applicationContext, Other::class.java )
            intent.putExtra("key", "Profile")
            startActivity( intent )
        }

        btnNewRecord.setOnClickListener {


            rewardedAd?.let { ad ->
                ad.show(this, OnUserEarnedRewardListener { rewardItem ->
                    // Handle the reward.
                    val rewardAmount = rewardItem.amount
                    val rewardType = rewardItem.type
                    Log.d(TAG, "User earned the reward.")
                })

                rewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                    override fun onAdClicked() {
                        // Called when a click is recorded for an ad.
                        Log.d(TAG, "Ad was clicked.")
                    }

                    override fun onAdDismissedFullScreenContent() {
                        val intent = Intent(applicationContext, Other::class.java)
                        intent.putExtra("key", "New")
                        startActivity(intent)
                        loadReward()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        // Called when ad fails to show.
                        Log.e(TAG, "Ad failed to show fullscreen content.")
                        rewardedAd = null
                    }

                    override fun onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        Log.d(TAG, "Ad recorded an impression.")
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d(TAG, "Ad showed fullscreen content.")
                    }
                }

            } ?: run {
                val intent = Intent(applicationContext, Other::class.java)
                intent.putExtra("key", "New")
                startActivity(intent)
            }

        }

    }


    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    private fun navAnim(view1 : TextView, id : Int){
        var drawable = resources.getDrawable(R.drawable.empty_home_ic)
        home.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)

        drawable = resources.getDrawable(R.drawable.empty_shield_search_ic)
        analysis.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)

        drawable = resources.getDrawable(R.drawable.empty_search_ic)
        search.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)

        drawable = resources.getDrawable(R.drawable.empty_settings_ic)
        settings.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)

        drawable = resources.getDrawable(id)
        view1.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)

    }

    fun loadReward() {

        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(this, getString(R.string.full_screen_ad_key) + stopAd, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                rewardedAd = null
            }

            override fun onAdLoaded(ad: RewardedAd) {
                rewardedAd = ad
            }
        })
    }

    private fun setFragment(fragment: Fragment){
        transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null) // Add the transaction to the back stack if needed
        transaction.commit()
    }

    companion object {
        val allPassList: MutableList<PassData> = mutableListOf()
        val allPassKey: MutableList<String> = mutableListOf()
        val allCategory: MutableList<String> = mutableListOf()
        lateinit var pro: ProgressBar
        var name: String = ""
        var email: String = ""
        var deviceName: String = ""
        var rewardedAd: RewardedAd? = null
        var stopAd: String = ""
        var isSubscribe = true

    }

}