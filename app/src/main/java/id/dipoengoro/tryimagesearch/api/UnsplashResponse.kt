package id.dipoengoro.tryimagesearch.api

import id.dipoengoro.tryimagesearch.data.UnsplashPhoto

data class UnsplashResponse(
    val results: List<UnsplashPhoto>
)