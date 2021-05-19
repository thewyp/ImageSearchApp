package com.thewyp.imagesearch.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.thewyp.imagesearch.api.UnsplashApi

private const val UNSPLASH_START_PAGE_INDEX = 1

class UnsplashDataSource(
    private val unsplashApi: UnsplashApi,
    private val query: String
) : PagingSource<Int, UnsplashPhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val position = params.key ?: UNSPLASH_START_PAGE_INDEX
        val searchPhotos = unsplashApi.searchPhotos(query, position, params.loadSize)
        val photos = searchPhotos.results
        return try {
            LoadResult.Page(
                data = photos,
                prevKey = if (params.key == UNSPLASH_START_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UnsplashPhoto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}