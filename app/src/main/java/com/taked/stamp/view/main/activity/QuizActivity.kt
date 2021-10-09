package com.taked.stamp.view.main.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.taked.stamp.R
import com.taked.stamp.databinding.ActivityQuizBinding
import com.taked.stamp.view.main.fragment.QuizFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizActivity: AppCompatActivity() {

    lateinit var binding: ActivityQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz)

        setSupportActionBar(binding.toolbar)
        val quizID = this.intent.getIntExtra("quizID", -1)
        supportActionBar?.apply {
            title = "謎解き${quizID + 1}"
            setDisplayHomeAsUpEnabled(true)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, QuizFragment(quizID))
                .commitNow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tollbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
