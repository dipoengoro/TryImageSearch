package id.dipoengoro.tryimagesearch.ui.search

import androidx.lifecycle.*
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import id.dipoengoro.tryimagesearch.data.UnsplashRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: UnsplashRepository,
    state: SavedStateHandle
) : ViewModel() {
    private val _currentQuery = state.getLiveData<String>(CURRENT_QUERY)
    val currentQuery get() = _currentQuery

    val photos = _currentQuery.switchMap {
        repository.getSearchResult(it).cachedIn(viewModelScope)
    }

    fun searchPhotos(query: String) {
        _currentQuery.value = query
    }

    companion object {
        private const val CURRENT_QUERY = "current_query"
    }
}
