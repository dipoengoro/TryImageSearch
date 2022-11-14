package id.dipoengoro.tryimagesearch.ui.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import id.dipoengoro.tryimagesearch.data.UnsplashRepository

class SearchViewModel @ViewModelScoped constructor(private val repository: UnsplashRepository) :
    ViewModel() {

    }