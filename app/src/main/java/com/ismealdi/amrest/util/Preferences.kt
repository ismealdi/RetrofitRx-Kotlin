package com.ismealdi.amrest.util

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by Al
 * on 22/04/19 | 14:08
 */
class Preferences(context: Context) {

    private var sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

    fun storeToken(string: String) {
        with (sharedPref.edit()) {
            putString(Constants.SHARED.TOKEN, string)
            apply()
        }
    }

    fun getToken() : String {
        return sharedPref.getString(Constants.SHARED.TOKEN, "")!!
    }
    
    fun storeName(string: String) {
        with (sharedPref.edit()) {
            putString(Constants.SHARED.NAME, string)
            apply()
        }
    }

    fun getName() : String {
        return sharedPref.getString(Constants.SHARED.NAME, "")!!
    }
}