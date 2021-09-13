package com.taked.stamp.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.taked.stamp.model.api.Message
import com.taked.stamp.view.main.fragment.info.InfoDataSource
import kotlinx.coroutines.flow.Flow

class InfoViewModel: ViewModel() {
    companion object {
        const val TEXT_TITLE = "インフォメーション"
    }

    val samplePagingFlow: Flow<PagingData<Message>> = Pager(
        PagingConfig(pageSize = 10, initialLoadSize = 10)
    ) {
        InfoDataSource(10)
    }.flow.cachedIn(viewModelScope)
}
