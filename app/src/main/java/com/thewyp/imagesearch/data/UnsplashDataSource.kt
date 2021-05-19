package com.thewyp.imagesearch.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.thewyp.imagesearch.api.UnsplashApi
import retrofit2.HttpException
import java.io.IOException

private const val UNSPLASH_START_PAGE_INDEX = 1

class UnsplashDataSource(
    private val unsplashApi: UnsplashApi,
    private val query: String
) : PagingSource<Int, UnsplashPhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val position = params.key ?: UNSPLASH_START_PAGE_INDEX
        return try {
            val searchPhotos = unsplashApi.searchPhotos(query, position, params.loadSize)
            val photos = searchPhotos.results
            LoadResult.Page(
                data = photos,
                prevKey = if (params.key == UNSPLASH_START_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UnsplashPhoto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}