package com.taked.stamp.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.taked.stamp.model.api.TimeEvent
import com.taked.stamp.view.main.fragment.schedule.SchedulePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(schedulePagingSource: SchedulePagingSource) : ViewModel() {
    val pagingFlow: Flow<PagingData<TimeEvent>> = Pager(
        PagingConfig(pageSize = 10)
    ) {
        schedulePagingSource
    }.flow.cachedIn(viewModelScope)
}
