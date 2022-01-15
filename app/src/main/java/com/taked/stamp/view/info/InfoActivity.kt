package com.taked.stamp.view.info

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taked.stamp.model.api.APIRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class InfoActivity : AppCompatActivity() {

    @Inject
    lateinit var apiRepository: APIRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val infoID = this.intent.getIntExtra("infoID", -1)
        val response = runBlocking { apiRepository.getInformationContent(infoID) }!!

        setContent {
            MainScreen(
                title = response.title,
                category = "[${response.category}]",
                message = if (response.message.isNotEmpty()) response.message else "本文無し"
            )
        }
    }


    @Composable
    fun MainScreen(title: String, category: String, message: String) {
        MaterialTheme {
            Scaffold(topBar = {
                TopAppBar(
                    title = { Text("インフォメーション詳細") },
                    navigationIcon = {
                        IconButton(onClick = { finish() }) {
                            Icon(Icons.Filled.ArrowBack, "backIcon")
                        }
                    })
            }, content = {
                LazyColumn(modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp)) {
                    item {
                        Text(text = category, fontSize = 20.sp)
                        Text(
                            text = title,
                            fontSize = 25.sp,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Divider(thickness = 2.dp, modifier = Modifier.padding(vertical = 20.dp))
                        Text(text = message)
                    }
                }
            })
        }
    }

    @Preview
    @Composable
    fun MainScreenPreView() {
        val title = "title"
        val category = "category"
        val message = "message"
        MainScreen(title = title, category = category, message = message)
    }
}
