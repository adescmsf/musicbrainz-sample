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
import com.mbrainz.sample.ui.common.hide
import com.mbrainz.sample.ui.common.show
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArtistDetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailArtistBinding

    private val args: ArtistDetailFragmentArgs by navArgs()
    private val releasesAdapter = ReleaseDetailAdapter()
    private val userErrorDisplay: UserErrorDisplay by inject()
    private val viewModel: ArtistDetailViewModel by viewModel()

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

    @Deprecated("Should be removed for next Android version")
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
            show()
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
        with(binding) {
            // handling card
            fragmentDetailCard.show()
            fragmentDetailTitle.text = result.name
            fragmentDetailSubtitle.isVisible = result.genre.isNotEmpty()
            fragmentDetailSubtitle.text = result.genre
            // handle information line
            val artistInformation = result.allInformation()
            fragmentDetailMore.isVisible = artistInformation.isNotEmpty()
            fragmentDetailMore.text = artistInformation

            // handle releases rv
            releasesAdapter.updateReleasesList(result.releases)
            if (result.releases.isNotEmpty()) {
                fragmentDetailReleasesRecyclerView.show()
                fragmentDetailHeader.show()
                fragmentDetailHeader.text = getString(R.string.fragment_detail_release_header, "${result.releases.size}")
            } else {
                fragmentDetailReleasesRecyclerView.hide()
                fragmentDetailHeader.hide()
            }

            fragmentDetailErrorTextview.hide()
            fragmentDetailRetryButton.hide()
            fragmentDetailProgressBar.hide()
        }
    }

    private fun setErrorState() {
        with(binding) {
            fragmentDetailErrorTextview.show()
            fragmentDetailRetryButton.show()

            fragmentDetailCard.hide()
            fragmentDetailReleasesRecyclerView.hide()
            fragmentDetailHeader.hide()
            fragmentDetailProgressBar.hide()
        }
    }

    private fun setLoadingState() {
        with(binding) {
            fragmentDetailProgressBar.show()

            fragmentDetailCard.hide()
            fragmentDetailReleasesRecyclerView.hide()
            fragmentDetailHeader.hide()
            fragmentDetailErrorTextview.hide()
            fragmentDetailRetryButton.hide()
        }
    }
}
