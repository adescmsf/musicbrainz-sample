package com.mbrainz.sample.ui.feature.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mbrainz.sample.common.trimChar
import com.mbrainz.sample.data.model.Artist
import com.mbrainz.sample.databinding.ItemSearchArtistBinding

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
        holder.bind(artists[position]) { selectedIndex ->
            onArtistSelected(artists[selectedIndex])
        }
    }

    override fun getItemCount() = artists.size

    fun updateArtistsList(newItems: List<Artist>) =
        artists.apply {
            notifyItemRangeRemoved(0, size)
            clear()
            addAll(newItems)
            notifyItemRangeInserted(0, size)
        }

    class ViewHolder(
        private val binding: ItemSearchArtistBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            artist: Artist,
            selectedListener: (index: Int) -> Unit
        ) {
            val genre = artist.genre.trimChar(16)
            with(binding) {
                itemSearchArtistName.text = artist.fullArtistName()
                itemSearchArtistGenre.isVisible = genre.isNotEmpty()
                itemSearchArtistGenre.text = genre
                root.setOnClickListener {
                    selectedListener(bindingAdapterPosition)
                }
            }
        }
    }
}
