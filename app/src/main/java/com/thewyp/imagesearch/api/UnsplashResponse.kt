package com.thewyp.imagesearch.api

import com.thewyp.imagesearch.data.UnsplashPhoto

data class UnsplashResponse(
    val results: List<UnsplashPhoto>
)