package com.taked.stamp_renew.viewmodel.util

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

class SharedPreferenceUtil {
    companion object {
        private const val name = "DataStore"
        private const val mode = AppCompatActivity.MODE_PRIVATE

        enum class SharedPreferenceKey(val key: String) {
            UUID("uuid"),
            PROGRESS("progress"),
            POSITION("position"),
            QUIZ("quiz"),
        }

        fun putInt(activity: Activity, sharedPreferenceKey: SharedPreferenceKey, data: Int) {
            activity.getSharedPreferences(name, mode).edit {
                putInt(sharedPreferenceKey.key, data)
            }
        }

        fun getInt(activity: Activity, sharedPreferenceKey: SharedPreferenceKey, defaultValue: Int) =
            activity.getSharedPreferences(name, mode).getInt(sharedPreferenceKey.key, defaultValue)

        fun putString(activity: Activity, sharedPreferenceKey: SharedPreferenceKey, data: String) {
            activity.getSharedPreferences(name, mode).edit {
                putString(sharedPreferenceKey.key, data)
            }
        }

        fun getString(activity: Activity, sharedPreferenceKey: SharedPreferenceKey, defaultValue: String) =
            activity.getSharedPreferences(name, mode).getString(sharedPreferenceKey.key, defaultValue)

    }
}