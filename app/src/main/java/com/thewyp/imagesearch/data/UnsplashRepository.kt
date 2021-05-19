package com.thewyp.imagesearch.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.thewyp.imagesearch.api.UnsplashApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject constructor(private val unsplashApi: UnsplashApi) {

    fun searchPhotos(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                UnsplashDataSource(unsplashApi, query)
            }
        ).liveData

}