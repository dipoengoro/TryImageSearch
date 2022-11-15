package id.dipoengoro.tryimagesearch.ui.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import id.dipoengoro.tryimagesearch.R
import id.dipoengoro.tryimagesearch.data.UnsplashPhoto
import id.dipoengoro.tryimagesearch.databinding.FragmentSearchBinding

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search),
    UnsplashPhotoAdapter.OnItemClickListener {

    private val viewModel by viewModels<SearchViewModel>()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)

        val adapter = UnsplashPhotoAdapter(this)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_search, menu)
                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as SearchView

                searchView.setOnQueryTextListener(object : OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (query != null) {
                            binding.recyclerViewSearch.scrollToPosition(0)
                            viewModel.searchPhotos(query)
                            searchView.clearFocus()
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean = false
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                when (menuItem.itemId) {
                    R.id.action_search -> {
                        true
                    }
                    else -> false
                }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.apply {
            recyclerViewSearch.setHasFixedSize(true)
            recyclerViewSearch.itemAnimator = null
            recyclerViewSearch.adapter = adapter.withLoadStateHeaderAndFooter(
                header = UnsplashPhotoLoadStateAdapter { adapter.retry() },
                footer = UnsplashPhotoLoadStateAdapter { adapter.retry() }
            )
            buttonRetrySearch.setOnClickListener {
                adapter.retry()
            }
        }
        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        viewModel.currentQuery.observe(viewLifecycleOwner) {
            binding.textOpeningSearch.isVisible = it == null
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressSearch.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerViewSearch.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetrySearch.isVisible = loadState.source.refresh is LoadState.Error
                textRetrySearch.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                    recyclerViewSearch.isVisible = false
                    textEmptySearch.isVisible = true
                } else {
                    textEmptySearch.isVisible = false
                }
            }
        }
    }

    override fun onItemClick(photo: UnsplashPhoto) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(
            photo,
            photo.user.username
        )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}