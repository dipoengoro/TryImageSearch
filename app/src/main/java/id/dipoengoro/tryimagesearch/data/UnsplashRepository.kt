package id.dipoengoro.tryimagesearch.data

import id.dipoengoro.tryimagesearch.api.UnsplashApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject constructor(private val unsplashApi: UnsplashApi)