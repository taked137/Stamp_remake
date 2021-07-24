package com.taked.stamp_renew.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.taked.stamp_renew.R
import com.taked.stamp_renew.ui.fragment.TitleFragment

class TitleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.title_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TitleFragment())
                .commitNow()
        }
    }
}