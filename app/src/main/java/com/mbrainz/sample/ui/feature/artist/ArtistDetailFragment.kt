package com.mbrainz.sample.ui.feature.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mbrainz.sample.R
import com.mbrainz.sample.data.model.Artist
import com.mbrainz.sample.databinding.FragmentDetailArtistBinding
import com.mbrainz.sample.ui.common.UserErrorDisplay
import org.koin.android.ext.android.inject

class ArtistDetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailArtistBinding

    private val args: ArtistDetailFragmentArgs by navArgs()
    private val releasesAdapter = ReleaseDetailAdapter()
    private val userErrorDisplay: UserErrorDisplay by inject()
    private val viewModel: ArtistDetailViewModel by inject()

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDetailArtistBinding.inflate(inflater, container, false)
        return binding.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.init()
        viewModel.retrieveArtistInformation(args.artistId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun FragmentDetailArtistBinding.init() {
        // TODO: code smell
        if (requireActivity() is AppCompatActivity) (requireActivity() as AppCompatActivity).setSupportActionBar(fragmentDetailToolbar)
        if (requireActivity() is AppCompatActivity) (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        fragmentDetailReleasesRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = releasesAdapter
            isVisible = true
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
        fragmentDetailRetryButton.setOnClickListener {
            viewModel.retrieveArtistInformation(args.artistId)
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.artist.collect { state ->
                when (state) {
                    is ArtistViewState.Success -> setResultState(state.result)
                    is ArtistViewState.Loading -> setLoadingState()
                    is ArtistViewState.Error -> {
                        userErrorDisplay.displayUserMessage(state.error)
                        setErrorState()
                    }
                }
            }
        }
    }

    private fun setResultState(result: Artist) {
        binding.apply {
            fragmentDetailErrorTextview.isVisible = false
            fragmentDetailRetryButton.isVisible = false
            fragmentDetailProgressBar.isVisible = false

            fragmentDetailCard.isVisible = true
            fragmentDetailTitle.text = result.name
            fragmentDetailSubtitle.isVisible = result.genre.isNotEmpty()
            fragmentDetailSubtitle.text = result.genre
            val artistInformations = result.allInformation()
            fragmentDetailMore.isVisible = artistInformations.isNotEmpty()
            fragmentDetailMore.text = artistInformations

            releasesAdapter.updateReleasesList(result.releases)
            if (result.releases.isNotEmpty()) {
                fragmentDetailReleasesRecyclerView.isVisible = true
                fragmentDetailHeader.isVisible = true
                fragmentDetailHeader.text = getString(R.string.fragment_detail_release_header, "${result.releases.size}")
            } else {
                fragmentDetailReleasesRecyclerView.isVisible = false
                fragmentDetailHeader.isVisible = false
            }
        }
    }

    private fun setErrorState() {
        binding.apply {
            fragmentDetailCard.isVisible = false
            fragmentDetailReleasesRecyclerView.isVisible = false
            fragmentDetailHeader.isVisible = false
            fragmentDetailProgressBar.isVisible = false

            fragmentDetailErrorTextview.isVisible = true
            fragmentDetailRetryButton.isVisible = true
        }
    }

    private fun setLoadingState() {
        binding.apply {
            fragmentDetailCard.isVisible = false
            fragmentDetailReleasesRecyclerView.isVisible = false
            fragmentDetailHeader.isVisible = false
            fragmentDetailErrorTextview.isVisible = false
            fragmentDetailRetryButton.isVisible = false

            fragmentDetailProgressBar.isVisible = true
        }
    }
}
