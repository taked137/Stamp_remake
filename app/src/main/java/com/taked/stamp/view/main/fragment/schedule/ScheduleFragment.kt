package com.taked.stamp.view.main.fragment.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.taked.stamp.model.api.APIRepository
import com.taked.stamp.viewmodel.main.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.time.LocalTime
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    @Inject
    lateinit var apiRepository: APIRepository
    private val viewModel: ScheduleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val eventTitles = runBlocking {
            apiRepository.getEventTitles()!!.events
        }

        return ComposeView(requireContext()).apply {
            setContent {
                InfoScreen(eventTitles)
            }
        }
    }

    @Composable
    fun TableCell(text: String, width: Dp, height: Dp, backgroundColor: Color = Color.White) {
        Column(
            modifier = Modifier
                .height(height)
                .width(width)
                .border(1.dp, Color.Black)
                .background(backgroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(width - 10.dp)
            )
        }
    }

    @Composable
    fun InfoScreen(eventTitles: List<String>) {
        val localTime = LocalTime.now()
        val verticalScrollState = rememberScrollState()
//        val horizontalScrollState = rememberLazyListState(localTime.hour)
        val horizontalScrollState = rememberLazyListState()

        val lazyPagingItems = viewModel.pagingFlow.collectAsLazyPagingItems()

        MaterialTheme {
            Column(modifier = Modifier.padding(16.dp)) {
                // header
                Row {
                    Row {
                        TableCell(
                            text = "時間",
                            width = 75.dp,
                            height = 40.dp,
                            backgroundColor = Color.LightGray
                        )
                    }
                    Row(Modifier.horizontalScroll(verticalScrollState)) {
                        eventTitles.forEach {
                            TableCell(
                                text = it,
                                width = 150.dp,
                                height = 40.dp,
                                backgroundColor = Color.Gray
                            )
                        }
                    }
                }

                // content
                LazyColumn {
                    items(lazyPagingItems) { event ->
                        Row {
                            TableCell(
                                text = "%5s".format("${event!!.time}:00").replace(" ", "0"),
                                width = 75.dp,
                                height = 85.dp,
                                backgroundColor =
                                if (event.time < 9 || event.time > 18) Color.LightGray
                                else Color(255, 165, 0)
                            )
                            Row(Modifier.horizontalScroll(verticalScrollState)) {
                                eventTitles.forEach {
                                    TableCell(
                                        text = event.events[it] ?: "",
                                        width = 150.dp,
                                        height = 85.dp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun InfoScreenPreView() {
        InfoScreen(listOf("abc", "12345", "99999999"))
    }
}
