package com.mbrainz.sample.ui.feature.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.mbrainz.sample.databinding.FragmentDetailArtistBinding
import com.mbrainz.sample.common.LocalLogger
import org.koin.android.ext.android.inject

class ArtistDetailFragment: Fragment() {
    private val args: ArtistDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentDetailArtistBinding
    private val logger: LocalLogger by inject()

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDetailArtistBinding.inflate(inflater, container, false)
        return binding.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.init()
    }

    private fun FragmentDetailArtistBinding.init() {
        logger.logInfo("coming from ${args.artistId}")
    }
}