package com.mbrainz.sample.ui.feature.artist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mbrainz.sample.data.model.Release
import com.mbrainz.sample.databinding.ItemDetailReleaseBinding

class ReleaseDetailAdapter : RecyclerView.Adapter<ReleaseDetailAdapter.ViewHolder>() {
    private val releases = mutableListOf<Release>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            ItemDetailReleaseBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(releases[position])
    }

    override fun getItemCount() = releases.size

    fun updateReleasesList(newItems: List<Release>) =
        releases.apply {
            notifyItemRangeRemoved(0, size)
            clear()
            addAll(newItems)
            notifyItemRangeInserted(0, size)
        }

    class ViewHolder(
        private val binding: ItemDetailReleaseBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(release: Release) {
            binding.itemResultRelease.text = release.fullName()
        }
    }
}
