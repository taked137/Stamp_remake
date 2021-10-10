package com.taked.stamp.view.main.fragment.info

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.taked.stamp.model.api.APIRepository
import com.taked.stamp.model.api.Message
import javax.inject.Inject

class InfoTitleDataSource @Inject constructor(
    private val apiRepository: APIRepository
) : PagingSource<Int, Message>() {

    companion object {
        const val LIMIT = 10
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Message> {
        val page = params.key ?: 0
        val response = apiRepository.getInformationTitle(limit = LIMIT, offset = page)?.result

        return if (response != null) {
            LoadResult.Page(
                data = response,
                nextKey = page + LIMIT,
                prevKey = page - LIMIT
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
