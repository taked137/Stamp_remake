package com.taked.stamp.view.main.fragment.info

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.taked.stamp.model.api.APIController
import com.taked.stamp.model.api.Message

class InfoDataSource(private val limit: Int) : PagingSource<Int, Message>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Message> {
        val page = params.key ?: 0
        val response = APIController.getInformation(limit = limit, offset = page)?.result

        return if (response != null) {
            LoadResult.Page(
                data = response,
                nextKey = page + limit,
                prevKey = page - limit
            )
        } else {
            LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Message>): Int? {
        return state.anchorPosition
    }
}
