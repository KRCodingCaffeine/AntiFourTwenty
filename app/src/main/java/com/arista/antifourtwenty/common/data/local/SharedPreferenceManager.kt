package com.arista.antifourtwenty.common.data.local

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager (context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFERENCE_NAME = "anti420"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_MOBILE = "user_mobile"
        private const val KEY_TWILIO_TOKEN = "twilio_token"
        private const val KEY_WALLET_BALANCE = "wallet_balance"
        private const val KEY_APP_CATEGORY = "app_category"
    }

    // Save login state
    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    // Retrieve login state
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Clear login state (used during logout)
    fun clearLoginState() {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, false).apply()
    }

    fun saveUserDetails(userId: Int, name: String, email: String, mobile: Long) {
        sharedPreferences.edit().apply {
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_EMAIL, email)
            putLong(KEY_USER_MOBILE, mobile)
            apply()
        }
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt(KEY_USER_ID, 0)
    }

    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_USER_NAME, null)
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString(KEY_USER_EMAIL, null)
    }

    fun getUserMobile(): Long {
        return sharedPreferences.getLong(KEY_USER_MOBILE, 0L)
    }

    // Save login state
    fun setTwilioToken(token: String) {
        sharedPreferences.edit().putString(KEY_TWILIO_TOKEN, token).apply()
    }

    // Retrieve login state
    fun getTwilioToken(): String? {
        return sharedPreferences.getString(KEY_TWILIO_TOKEN, "")
    }

    fun saveUserWalletDetails(userId: Int, name: String, email: String, mobile: Long, walletBalance:Long, appCategory:String) {
        sharedPreferences.edit().apply {
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_EMAIL, email)
            putLong(KEY_USER_MOBILE, mobile)
            putLong(KEY_WALLET_BALANCE, walletBalance)
            putString(KEY_APP_CATEGORY, appCategory)
            apply()
        }
    }

    fun getWalletBalance() : Long?{
        return sharedPreferences.getLong(KEY_WALLET_BALANCE, 0)
    }

    fun setWalletBalance(walletBalance: Long) {
        sharedPreferences.edit().putLong(KEY_WALLET_BALANCE, walletBalance).apply()
    }

    fun getAppCategory(): String? {
        return sharedPreferences.getString(KEY_APP_CATEGORY, "base")
    }

    fun setAppCategory(appCategory: String) {
        sharedPreferences.edit().putString(KEY_APP_CATEGORY, appCategory).apply()
    }
}