package com.taked.stamp_renew.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.taked.stamp_renew.R
import com.taked.stamp_renew.ui.fragment.TitleFragment

class TitleActivity : AppCompatActivity() {

    private fun getState(): ActivityState {
        val dataStore = getSharedPreferences("DataStore", MODE_PRIVATE)
        return when (dataStore.getInt("state", -1)) {
            ActivityState.GAME.value -> ActivityState.GAME
            ActivityState.CLEAR.value -> ActivityState.CLEAR
            else -> ActivityState.REGISTER
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getState() != ActivityState.REGISTER) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            setContentView(R.layout.activity_title)
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, TitleFragment())
                    .commitNow()
            }
        }
    }
}