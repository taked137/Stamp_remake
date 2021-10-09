package com.taked.stamp.view.title.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.taked.stamp.R
import com.taked.stamp.view.main.ActivityState
import com.taked.stamp.viewmodel.util.SharedPreferenceUtil.SharedPreferenceKey
import com.taked.stamp.view.main.activity.MainActivity
import com.taked.stamp.view.title.fragment.TitleFragment
import com.taked.stamp.viewmodel.util.SharedPreferenceUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
