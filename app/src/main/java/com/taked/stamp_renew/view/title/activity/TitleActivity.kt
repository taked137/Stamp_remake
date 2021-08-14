package com.taked.stamp_renew.view.title.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.taked.stamp_renew.R
import com.taked.stamp_renew.view.main.ActivityState
import com.taked.stamp_renew.viewmodel.util.SharedPreferenceUtil.Companion.SharedPreferenceKey
import com.taked.stamp_renew.view.main.activity.MainActivity
import com.taked.stamp_renew.view.title.fragment.TitleFragment
import com.taked.stamp_renew.viewmodel.util.SharedPreferenceUtil

class TitleActivity : AppCompatActivity() {

    private fun getState(): ActivityState =
        when (SharedPreferenceUtil.getInt(this, SharedPreferenceKey.PROGRESS, -1)) {
            ActivityState.GAME.value -> ActivityState.GAME
            ActivityState.CLEAR.value -> ActivityState.CLEAR
            else -> ActivityState.REGISTER
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getState() != ActivityState.REGISTER) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
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