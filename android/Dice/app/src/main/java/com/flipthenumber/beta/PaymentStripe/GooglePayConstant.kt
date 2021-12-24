package com.flipthenumber.beta.PaymentStripe

import com.google.android.gms.wallet.WalletConstants

object GooglePayConstant {
    /**
     * ENVIRONMENT_PRODUCTION will make the API return chargeable card information.
     */
    //pk_test_51HRYGUDE7aXkljMPwJEDRsbM2kDjzTC5MpzY6PMmJGTU8g7ym9yEsHNwiLhA5YKviK394nfhunNraZqIMvV5mUtp00YyJf1gkc
    //pk_test_51HRYGUDE7aXkljMPwJEDRsbM2kDjzTC5MpzY6PMmJGTU8g7ym9yEsHNwiLhA5YKviK394nfhunNraZqIMvV5mUtp00YyJf1gkc
    const val PAYMENTS_ENVIRONMENT = WalletConstants.ENVIRONMENT_PRODUCTION
    const val PAYMENTS_GATEWAY_KEY = "gateway"
    const val PAYMENTS_GATEWAY_VAL = "stripe"
    const val STRIPE_KEY_PUBLISHABLE_KEY = "stripe:publishableKey"
    const val STRIPE_VAL_PUBLISHABLE_VAL = "pk_live_51HRYGUDE7aXkljMPcP6Q7zJkUWjthEb7sJC33X8sY3Kvxr1YM7JGKomu6dZtM6uxS9kfZIdzFORHZbDrZfDQbqKB00yRsU74Rm"
    const val STRIPE_VERSION_KEY = "stripe:version"
    const val STRIPE_VERSION_VAL = "2018-11-08"
    const val CURRENCY_CODE = "INR"


}