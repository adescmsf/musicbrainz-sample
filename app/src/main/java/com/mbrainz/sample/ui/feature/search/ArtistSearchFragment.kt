package com.mbrainz.sample.ui.feature.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mbrainz.sample.data.model.Artist
import com.mbrainz.sample.databinding.FragmentSearchArtistBinding
import com.mbrainz.sample.common.Logger
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArtistSearchFragment: Fragment() {
    private lateinit var binding: FragmentSearchArtistBinding
    private lateinit var artistsAdapter: ArtistSearchAdapter
    private val logger: Logger by inject()
    private val viewModel: ArtistSearchViewModel by viewModel()

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchArtistBinding.inflate(inflater, container, false)
        return binding.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        artistsAdapter = ArtistSearchAdapter(onArtistSelected = { artistSelected ->
            navigateDetails(artistSelected)
        })
        binding.init()
    }

    private fun FragmentSearchArtistBinding.init() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(fragmentSearchToolbar)
        fragmentSearchResultRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = artistsAdapter
            isVisible = true
        }
        fragmentSearchStringEditText.doOnTextChanged { text, _, _, _ ->
            text?.let { searchText ->
                if (searchText.length > 2) viewModel.searchArtist(searchText.toString())
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.searchedArtist.collect { state ->
                when (state) {
                    is ArtistSearchState.Success -> {
                        if (state.result.isNotEmpty()) {
                            setResultState(state.result)
                        } else {
                            setEmptyState()
                        }
                    }
                    is ArtistSearchState.Loading -> {
                        setLoadingState()
                    }
                    is ArtistSearchState.Error -> {
                        // display toast
                        setErrorState()
                    }
                }
            }
        }
    }

    private fun setErrorState() {

    }

    private fun setEmptyState() {
        artistsAdapter.updateArtistsList(emptyList())
        binding.fragmentSearchRvHeader.isVisible = false
        binding.fragmentSearchResultRecyclerView.isVisible = false
    }

    private fun setResultState(result: List<Artist>) {
        binding.fragmentSearchRvHeader.apply {
            isVisible = true
            text = "Artists ${result.size}"
        }
        binding.fragmentSearchResultRecyclerView.isVisible = true
        artistsAdapter.updateArtistsList(result)
    }

    private fun setLoadingState() {
        binding.fragmentSearchRvHeader.isVisible = false
        binding.fragmentSearchResultRecyclerView.isVisible = false
    }

    private fun navigateDetails(artist: Artist) {
        findNavController().navigate(ArtistSearchFragmentDirections.actionSearchArtistFragmentToDetailArtistFragment(artist.id))
    }
}