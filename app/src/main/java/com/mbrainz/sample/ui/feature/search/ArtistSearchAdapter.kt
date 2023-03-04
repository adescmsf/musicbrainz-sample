package com.mbrainz.sample.ui.feature.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mbrainz.sample.data.model.Artist
import com.mbrainz.sample.databinding.ItemSearchArtistBinding
import com.mbrainz.sample.ui.common.trimChar

class ArtistSearchAdapter(
    private val onArtistSelected: (Artist) -> Unit
) : RecyclerView.Adapter<ArtistSearchAdapter.ViewHolder>() {
    private val artists = mutableListOf<Artist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            ItemSearchArtistBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(artists[position])
        holder.itemView.setOnClickListener {
            onArtistSelected(artists[position])
        }
    }

    override fun getItemCount(): Int {
        return artists.size
    }

    fun updateArtistsList(newItems: List<Artist>) {
        notifyItemRangeRemoved(0, artists.size)
        artists.apply {
            clear()
            addAll(newItems)
        }
        notifyItemRangeInserted(0, artists.size)
    }

    class ViewHolder(
        private val binding: ItemSearchArtistBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: Artist) {
            val genre = artist.genre.trimChar(16)
            binding.apply {
                itemSearchArtistName.text = artist.fullArtistName()
                itemSearchArtistGenre.isVisible = genre.isNotEmpty()
                itemSearchArtistGenre.text = genre
            }
        }
    }
}