package com.taked.stamp.view.main.activity

import android.os.Bundle
import android.util.Log
import com.taked.stamp.R
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.taked.stamp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        val navController = navHostFragment!!.findNavController()
        binding.bottomNav.setupWithNavController(navController)
    }

    // https://stackoverflow.com/questions/57187628/navigation-components-onsupportnavigateup
    //override fun onSupportNavigateUp() = findNavController(R.id.fragmentContainerView).navigateUp()
}
