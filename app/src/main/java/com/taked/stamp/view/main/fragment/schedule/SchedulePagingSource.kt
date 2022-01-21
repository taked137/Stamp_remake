package com.taked.stamp.view.main.fragment.schedule

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.taked.stamp.model.api.APIRepository
import com.taked.stamp.model.api.TimeEvent
import com.taked.stamp.view.main.fragment.info.InfoTitleDataSource
import javax.inject.Inject

class SchedulePagingSource @Inject constructor(
    private val apiRepository: APIRepository
) : PagingSource<Int, TimeEvent>() {

    companion object {
        const val LIMIT = 10
    }

    override fun getRefreshKey(state: PagingState<Int, TimeEvent>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TimeEvent> {
        val page = params.key ?: 0
        val response = apiRepository.getTimeTable(
            limit = LIMIT,
            offset = page
        )?.result

        return if (response != null) {
            LoadResult.Page(
                data = response,
                nextKey = page + InfoTitleDataSource.LIMIT,
                prevKey = page - InfoTitleDataSource.LIMIT
            )
        } else {
            LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        }
    }
}
