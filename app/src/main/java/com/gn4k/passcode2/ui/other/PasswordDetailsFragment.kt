package com.gn4k.passcode2.ui.other

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gn4k.passcode2.R
import com.gn4k.passcode2.data.BrandData
import com.gn4k.passcode2.ui.home.Home
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.slider.Slider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Base64
import kotlin.random.Random


class PasswordDetailsFragment : Fragment() {


    lateinit var view1 : View

    lateinit var tvName: TextView
    lateinit var tvId: TextView
    lateinit var tvId2: TextView
    lateinit var tvPassword: TextView
    lateinit var imgLogo: ImageView
    lateinit var btnCopy: Button
    lateinit var btnChangePassword: Button
    lateinit var btnDelete: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view1 = inflater.inflate(R.layout.fragment_password_details, container, false)

        context?.let {
            MobileAds.initialize(it, object : OnInitializationCompleteListener {
                override fun onInitializationComplete(initializationStatus: InitializationStatus) {
//                    Toast.makeText(this, " successful ", Toast.LENGTH_SHORT).show()
                }
            })
        }

        val mAdView: AdView
        mAdView = view1.findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        tvName = view1.findViewById(R.id.tvName)
        tvPassword = view1.findViewById(R.id.tvPassword)
        tvId2 = view1.findViewById(R.id.tvUserId2)
        tvId = view1.findViewById(R.id.tvUserId)
        btnCopy = view1.findViewById(R.id.btnCopyPass)
        btnChangePassword = view1.findViewById(R.id.btnChangePass)
        btnDelete = view1.findViewById(R.id.btnDelete)
        imgLogo = view1.findViewById(R.id.imgLogo)

        SetUI()

        btnCopy.setOnClickListener {

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
                            context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clipData = ClipData.newPlainText("Copied Text", Other.detailPass)
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
                    context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("Copied Text", Other.detailPass)
                clipboardManager.setPrimaryClip(clipData)
            }
        }

        btnChangePassword.setOnClickListener {
            changePasswordDialog()
        }

        btnDelete.setOnClickListener {
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val reference: DatabaseReference = database.reference
                .child("users")
                .child(loadLocalData())
                .child("password")
                .child(Other.detailCategory)
                .child(Other.detailKey)
            reference.removeValue()
            sendPassToDeleted()
            activity?.onBackPressed();
        }

        return view1
    }

    fun SetUI(){

        val brandData = BrandData()
        tvPassword.text = Other.detailPass
        tvName.text = Other.detailName
        tvId.text = Other.detailId
        tvId2.text = Other.detailId
        imgLogo.setImageResource(brandData.logoArray[Other.logoIndex.toInt()]);

    }

    fun loadLocalData() : String{
        val sharedPreferences = context?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("userName", null).toString()
        return id
    }

    fun sendPassToDeleted() {

            // Get a reference to the database
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("users").child(loadLocalData()).child("deletedPassword").child(Other.detailCategory)
            val encodedPass = Base64.getEncoder().encodeToString(Other.detailPass.toByteArray())
            // Create a Map to represent your data
            val userData = mapOf(
                "name" to Other.detailName,
                "logoIndex" to Other.logoIndex,
                "userId" to Other.detailId,
                "password" to encodedPass
            )

            // Push the data to the database
            val key = myRef.push().key
            if (key != null) {
                myRef.child(key).setValue(userData)
            }
            Toast.makeText(context, "Password Moved to Recycle Bin Successfully", Toast.LENGTH_SHORT).show()
            activity?.onBackPressed();

    }


    fun changePasswordDialog() {
        val dialog = context?.let { Dialog(it, R.style.AppBottomSheetDialogTheme) }
        dialog?.setContentView(R.layout.change_password)

        lateinit var tvLetterCount: TextView
        lateinit var isLowerCase: CheckBox
        lateinit var isNumber: CheckBox
        lateinit var isSymbols: CheckBox
        lateinit var isUpperCase: CheckBox
        lateinit var passStrongness: ProgressBar
        lateinit var btnGeneratePassword: Button
        lateinit var slider: Slider

        val save : Button? = dialog?.findViewById(R.id.sendBtn);
        val edPassword : EditText? = dialog?.findViewById(R.id.edPassword);
        val cancel : Button? = dialog?.findViewById(R.id.cancelBtn);

        var LowerCase = false
        var Number = false
        var Symbols = false
        var UpperCase = false


        if (dialog != null) {
            isLowerCase = dialog.findViewById(R.id.isLowerCase)
            isNumber = dialog.findViewById(R.id.isNumber)
            isSymbols = dialog.findViewById(R.id.isSymbols)
            isUpperCase = dialog.findViewById(R.id.isUpperCase)
            passStrongness = dialog.findViewById(R.id.passStrongness)
            btnGeneratePassword = dialog.findViewById(R.id.btnGeneratePassword)
            slider = dialog.findViewById<Slider>(R.id.numberPicker)
            tvLetterCount = dialog.findViewById(R.id.tvLetterCount)
        }

        slider.addOnChangeListener { slider, value, fromUser ->
            val count =  value.toInt()
            tvLetterCount.text = count.toString()
        }

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
                if (edPassword != null) {
                    edPassword.setText(it)
                }
            }

        }

        if (save != null) {
            save.setOnClickListener {

                val password = edPassword?.text.toString()
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("users")
                    .child(loadLocalData())
                    .child("password")
                    .child(Other.detailCategory)
                    .child(Other.detailKey)

                val encodedPass = Base64.getEncoder().encodeToString(edPassword?.text.toString().toByteArray())


                // Create a Map to represent your data
                val userData = mapOf(
                    "password" to encodedPass

                )

                // Push the data to the database
                myRef.updateChildren(userData)
                SetUI()
                dialog.cancel()
            }
        }

        if (cancel != null) {
            cancel.setOnClickListener { dialog.cancel() }
        }

        dialog?.setCancelable(true)
        dialog?.show()
    }

    fun loadReward() {

        val adRequest = AdRequest.Builder().build()
        context?.let {
            RewardedAd.load(it, getString(R.string.full_screen_ad_key) + Home.stopAd, adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Home.rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    Home.rewardedAd = ad
                }
            })
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


    companion object {

    }
}