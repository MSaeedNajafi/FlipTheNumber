package com.flipthenumber.beta.PaymentStripe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flipthenumber.beta.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentsClient
import com.stripe.android.model.Token

private const val LOAD_PAYMENT_DATA_REQUEST_CODE = 100
private const val TAG = "GOOGLE_PAY_DEMO"

class ActivityPayment : AppCompatActivity() {
    private lateinit var mPaymentsClient: PaymentsClient
    private var button_buy: RelativeLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pay_with_google_pay)
        button_buy = findViewById<RelativeLayout>(R.id.layout_google_pay)

        mPaymentsClient = GooglePayUtil.createPaymentsClient(this)

        isReadyToPay()

        InitForGooglePay()

        button_buy!!.setOnClickListener {

        }

    }

    private fun InitForGooglePay() {
        val priceInfo = PriceInfoModel("1.00", GooglePayConstant.CURRENCY_CODE)
        val request = GooglePayUtil.createPaymentDataRequest(priceInfo)

        button_buy!!.isClickable = false

        AutoResolveHelper.resolveTask(
            GooglePayUtil.createPaymentsClient(this).loadPaymentData(request),
            this@ActivityPayment,
            LOAD_PAYMENT_DATA_REQUEST_CODE)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(TAG, "onActivityResult: requestCode = $requestCode, resultCode = $resultCode")

        when (requestCode) {

            LOAD_PAYMENT_DATA_REQUEST_CODE -> {

                when (resultCode) {
                    Activity.RESULT_OK -> {
                        if (data != null) {
                            val paymentData = PaymentData.getFromIntent(data)
                            // You can get some data on the user's card, such as the brand and last 4 digits
                            val info = paymentData!!.cardInfo
                            // You can also pull the user address from the PaymentData object.
                            val address = paymentData.shippingAddress
                            // This is the raw JSON string version of your Stripe token.
                            val rawToken = paymentData.paymentMethodToken?.token



                            //HIDE Code>>

                            val stripeToken = Token.fromString(rawToken)
                            if (stripeToken != null) {
                                Log.d(TAG, "stripeToken = ${stripeToken.id}")
                            } else {
                                Log.d(TAG, "stripeToken is null in Activity.RESULT_OK")
                            }


                        }
                        Log.d(TAG, "data is null in Activity.RESULT_OK")
                    }

                    Activity.RESULT_CANCELED -> {
                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        // Log the status for debugging
                        // Generally there is no need to show an error to
                        // the user as the Google Payment API will do that
                        val status = AutoResolveHelper.getStatusFromIntent(data)
                        Log.w("loadPaymentData failed", String.format("Error code: %d", status?.statusCode))
                    }
                }

                // Re-enables the Google Pay payment button.
                button_buy!!.isClickable = true
            }
        }
        // Handle any other startActivityForResult calls you may have made.
    }

    private fun isReadyToPay() {
        val request = GooglePayUtil.isReadyToPayRequest()

        GooglePayUtil.createPaymentsClient(this).isReadyToPay(request)    // type: Task<Boolean>
            ?.addOnCompleteListener {
                try {
                    val isGooglePayEnable = it.getResult(ApiException::class.java)

                    if (isGooglePayEnable == true) {
                        //show Google as payment option
                      //  button_buy.isVisible = isGooglePayEnable

                    } else {
                        //hide Google as payment option
                      //  button_buy.isVisible = false

                        Toast.makeText(this, "Google Pay is not available on this device", Toast.LENGTH_LONG).show()
                        Log.d(TAG, "Google Pay is not available on this device")
                    }
                } catch (exception: ApiException) {
                }
            }
    }




}