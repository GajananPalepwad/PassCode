package com.gn4k.passcode2.ui.home.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.gn4k.passcode2.R
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ImmutableList
import java.util.concurrent.Executors

class Premium : AppCompatActivity() {

    private var billingClient: BillingClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premium)

        val sendBtn: Button = findViewById(R.id.sendBtn)
        val btnBack: TextView = findViewById(R.id.btnBack)

        sendBtn.setOnClickListener {
            Toast.makeText(applicationContext, "Yuppppp", Toast.LENGTH_SHORT).show()
        }

        btnBack.setOnClickListener { onBackPressed() }
        intiBillingClint()

    }

    private fun intiBillingClint(){
        billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                }
            }
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            // To be implemented in a later section.
        }

    private fun getPrice() {
            billingClient!!.startConnection(object: BillingClientStateListener {
                override fun onBillingServiceDisconnected() {}
                override fun onBillingSetupFinished(bit1ingResutt: BillingResult) {
                    val executorService = Executors.newSingleThreadExecutor()
                    executorService.execute {
                        val queryProductDetailsParams =
                            QueryProductDetailsParams.newBuilder().setProductList(
                                ImmutableList.of(
                                    QueryProductDetailsParams.Product.newBuilder()
                                        .setProductId("pro")
                                        .setProductType(BillingClient.ProductType.SUBS)
                                        .build()
                                )
                            ).build()
                        billingClient!!.queryProductDetailsAsync(
                            queryProductDetailsParams
                        ) { _, productDetailsList ->
                            for (productDetails in productDetailsList) {
//                                subsName = productDetails.name
//                                des = productDetails.description
                                var formattedPrice =
                                    productDetails.subscriptionOfferDetails!![0].pricingPhases
                                        .pricingPhaseList[0].formattedPrice
                                val billingPeriod =
                                    productDetails.subscriptionOfferDetails!![0].pricingPhases
                                        .pricingPhaseList[0].billingPeriod
                                val recurrenceMode =
                                    productDetails.subscriptionOfferDetails!![0].pricingPhases
                                        .pricingPhaseList[0].recurrenceMode
                                val bp: String = billingPeriod
                                val n: String = billingPeriod.substring(1, 2)
                                val duration: String = billingPeriod.substring(2, 3)

//                                if (recurrenceMode == 2) {
//                                    when (duration) {
//                                        "M" -> dur = "For $n Month "
//                                        "Y" -> dur = "For $n Year "
//                                        "W" -> dur = "For $n Week "
//                                        "D" -> dur = "For $n Days "
//
//                                    }
//                                } else {
//                                    when (bp) {
//                                        "P1M" -> dur = "/Monthly"
//                                        "P6M" -> dur = "/Every 6 Month"
//                                        "P1Y" -> dur = "/Yearly"
//                                        "P1W" -> dur = "/Weekly"
//                                        "P3W" -> dur = "Every /3 Week"
//
//                                    }
//                                }

//                                    phases = "$formattedPrice $dur"
//                                    for (i in 0 <=..<= productDetails.subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList
                            }

                        }

                    }
                }
            })
    }



}