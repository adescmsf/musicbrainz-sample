package com.mbrainz.sample.ui.feature.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mbrainz.sample.R
import com.mbrainz.sample.data.model.Artist
import com.mbrainz.sample.databinding.FragmentSearchArtistBinding
import com.mbrainz.sample.ui.common.UserErrorDisplay
import com.mbrainz.sample.ui.common.hide
import com.mbrainz.sample.ui.common.show
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArtistSearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchArtistBinding
    private lateinit var artistsAdapter: ArtistSearchAdapter
    private val userErrorDisplay: UserErrorDisplay by inject()
    private val viewModel: ArtistSearchViewModel by viewModel()

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchArtistBinding.inflate(inflater, container, false)
        return binding.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        artistsAdapter = ArtistSearchAdapter(
            onArtistSelected = { artistSelected ->
                navigateDetails(artistSelected)
            }
        )
        binding.init()
    }

    private fun FragmentSearchArtistBinding.init() {
        // TODO: code smell
        if (requireActivity() is AppCompatActivity) (requireActivity() as AppCompatActivity).setSupportActionBar(fragmentSearchToolbar)
        fragmentSearchResultRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = artistsAdapter
            show()
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
        fragmentSearchStringEditText.doOnTextChanged { text, _, _, _ ->
            text?.let { searchText ->
                viewModel.searchArtist(searchText.toString())
            }
        }
        fragmentSearchRetryButton.setOnClickListener {
            viewModel.searchArtist(fragmentSearchStringEditText.text.toString())
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
                    is ArtistSearchState.Loading -> setLoadingState()

                    is ArtistSearchState.Error -> {
                        userErrorDisplay.displayUserMessage(state.error)
                        setErrorState()
                    }
                }
            }
        }
    }

    private fun setErrorState() {
        with(binding) {
            fragmentSearchErrorTextview.show()
            fragmentSearchRetryButton.show()
            fragmentSearchStringEditText.clearFocus()

            fragmentSearchRvHeader.hide()
            fragmentSearchResultRecyclerView.hide()
            fragmentSearchProgressBar.hide()
        }
    }

    private fun setEmptyState() {
        artistsAdapter.updateArtistsList(emptyList())
        with(binding) {
            fragmentSearchRvHeader.hide()
            fragmentSearchResultRecyclerView.hide()
            fragmentSearchProgressBar.hide()
            fragmentSearchErrorTextview.hide()
            fragmentSearchRetryButton.hide()
        }
    }

    private fun setResultState(result: List<Artist>) {
        with(binding) {
            fragmentSearchRvHeader.text = getString(R.string.fragment_search_result_header, "${result.size}")
            fragmentSearchRvHeader.show()
            fragmentSearchResultRecyclerView.show()

            fragmentSearchProgressBar.hide()
            fragmentSearchErrorTextview.hide()
            fragmentSearchRetryButton.hide()
        }
        artistsAdapter.updateArtistsList(result)
    }

    private fun setLoadingState() {
        with(binding) {
            fragmentSearchProgressBar.show()

            fragmentSearchRvHeader.hide()
            fragmentSearchResultRecyclerView.hide()
            fragmentSearchErrorTextview.hide()
            fragmentSearchRetryButton.hide()
        }
    }

    private fun navigateDetails(artist: Artist) {
        findNavController().navigate(ArtistSearchFragmentDirections.actionSearchArtistFragmentToDetailArtistFragment(artist.id))
    }
}
