package com.taked.stamp.view.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import java.time.LocalTime

class ScheduleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                InfoScreen()
            }
        }
    }

    @Composable
    fun TableCell(text: String, width: Dp, height: Dp) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .border(1.dp, Color.Black)
                .width(width)
                .padding(vertical = height * 2)
        )
    }

    @Composable
    fun InfoScreen() {
        val tableData = (0..24).mapIndexed { index, item ->
            "%5s".format("$index:00").replace(" ", "0") to "Item $item"
        }

        val localTime = LocalTime.now()
        val verticalScrollState = rememberScrollState()
        val horizontalScrollState = rememberLazyListState(localTime.hour)

        MaterialTheme {
            Column(modifier = Modifier.padding(16.dp)) {
                // header
                Row(Modifier.background(Color.Gray)) {
                    Row {
                        TableCell(text = "Column", width = 75.dp, height = 4.dp)
                    }
                    Row(Modifier.horizontalScroll(verticalScrollState)) {
                        for (i in 2..10) {
                            TableCell(text = "Column 2", width = 150.dp, height = 4.dp)
                        }
                    }
                }

                // content
                LazyColumn(state = horizontalScrollState) {
                    items(tableData) {
                        val (time, text) = it
                        Row {
                            TableCell(text = time, width = 75.dp, height = 17.dp)
                            Row(Modifier.horizontalScroll(verticalScrollState)) {
                                for (i in 2..10) {
                                    TableCell(text = text, width = 150.dp, height = 17.dp)
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
        InfoScreen()
    }
}
