package com.flipthenumber.beta.PaymentStripe

import android.app.Activity
import com.google.android.gms.wallet.*
import java.util.*

object GooglePayUtil {

    /**
     * Creates an instance of [PaymentsClient] for use in an [Activity]
     * using the environment and theme set in [Constants].
     */
    fun createPaymentsClient(activity: Activity): PaymentsClient {
        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(GooglePayConstant.PAYMENTS_ENVIRONMENT)
            .build()

        return Wallet.getPaymentsClient(activity, walletOptions)
    }

    /**
     * Check if user's device could support google pay
     */
    fun isReadyToPayRequest(): IsReadyToPayRequest =
        IsReadyToPayRequest.newBuilder()
            .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
            .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
            .build()

    /**
     * Create a PaymentMethodTokenizationParameters object with your credentials for using Stripe as gateway
     */
    private fun createTokenizationParameters(): PaymentMethodTokenizationParameters {
        return PaymentMethodTokenizationParameters.newBuilder()
            .setPaymentMethodTokenizationType(WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY)
            .addParameter(GooglePayConstant.PAYMENTS_GATEWAY_KEY, GooglePayConstant.PAYMENTS_GATEWAY_VAL)
            .addParameter(GooglePayConstant.STRIPE_KEY_PUBLISHABLE_KEY, GooglePayConstant.STRIPE_VAL_PUBLISHABLE_VAL)
            .addParameter(GooglePayConstant.STRIPE_VERSION_KEY, GooglePayConstant.STRIPE_VERSION_VAL)
            .build()
    }

    /**
     * Create the PaymentDataRequest object with the information relevant to the purchase.
     * Including price, allowable payment methods and tokenizationParameters (created by createTokenizationParameters())
     */
    fun createPaymentDataRequest(priceInfo: PriceInfoModel): PaymentDataRequest {
        return PaymentDataRequest.newBuilder()

            // Create transaction info (including price and currency)
            .setTransactionInfo(
                TransactionInfo.newBuilder()
                    .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                    .setTotalPrice(priceInfo.price)   // set price
                    .setCurrencyCode(priceInfo.currency)
                    .build()
            )

            // Set allowed payment method
            .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
            .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)

            // Define supported card networks
            .setCardRequirements(
                CardRequirements.newBuilder()
                    .addAllowedCardNetworks(
                        Arrays.asList(
                            WalletConstants.CARD_NETWORK_AMEX,
                            WalletConstants.CARD_NETWORK_DISCOVER,
                            WalletConstants.CARD_NETWORK_VISA,
                            WalletConstants.CARD_NETWORK_MASTERCARD))
                    .build()
            )

            // Choose Stripe as a gateway (Could choose DIRECT or GATEWAY as payment tokenization method
            .setPaymentMethodTokenizationParameters(createTokenizationParameters())
            .build()
    }

}