package com.example.natour.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.natour.data.model.TrailPhoto
import com.example.natour.databinding.TrailPhotoViewBinding

class TrailPhotoListAdapter(
    private val trailPhotoClickListener: (TrailPhoto) -> Unit
) : ListAdapter<TrailPhoto, TrailPhotoListAdapter.TrailPhotoViewHolder>(TrailDiffCallback) {

    companion object {
        private val TrailDiffCallback = object : DiffUtil.ItemCallback<TrailPhoto>() {
            override fun areItemsTheSame(oldItem: TrailPhoto, newItem: TrailPhoto): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: TrailPhoto, newItem: TrailPhoto): Boolean =
                oldItem == newItem
        }
    }

    inner class TrailPhotoViewHolder(
      private val binding: TrailPhotoViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindTrailPhoto(trailPhoto: TrailPhoto) {
            binding.trailPhoto.setImageDrawable(trailPhoto.image)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrailPhotoViewHolder {
        val binding = TrailPhotoViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        val viewHolder = TrailPhotoViewHolder(binding)

        binding.trailPhoto.setOnClickListener {
            val position = viewHolder.adapterPosition
            trailPhotoClickListener(getItem(position))
        }

        return viewHolder
    }

    override fun onBindViewHolder(trailPhotoViewHolder: TrailPhotoViewHolder, position: Int) {
        trailPhotoViewHolder.bindTrailPhoto(getItem(position))
    }
}