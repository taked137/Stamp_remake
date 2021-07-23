package com.taked.stamp_renew

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.taked.stamp_renew.fragment.RegisterFragment
import com.taked.stamp_renew.fragment.TitleFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, RegisterFragment.newInstance())
                .replace(R.id.container, TitleFragment())
                .commitNow()
        }
    }
}