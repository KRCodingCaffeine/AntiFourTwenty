package com.arista.antifourtwenty.common.data.local

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager (context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFERENCE_NAME = "anti420"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
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
}