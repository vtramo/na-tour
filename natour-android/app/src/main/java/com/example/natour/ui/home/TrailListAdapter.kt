package com.example.natour.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.natour.MainActivity
import com.example.natour.R
import com.example.natour.data.model.Trail
import com.example.natour.databinding.TrailCardViewBinding

class TrailListAdapter(
    private val trailCardClickListener: (Trail) -> Unit,
    private val favoriteTrailClickListener: (Trail, ImageButton) -> Unit
): ListAdapter<Trail, TrailListAdapter.TrailCardViewHolder>(TrailDiffCallback) {

    companion object {
        private val TrailDiffCallback = object : DiffUtil.ItemCallback<Trail>() {
            override fun areItemsTheSame(oldItem: Trail, newItem: Trail): Boolean =
                oldItem.idTrail == newItem.idTrail

            override fun areContentsTheSame(oldItem: Trail, newItem: Trail): Boolean =
                oldItem == newItem
        }
    }

    inner class TrailCardViewHolder(
        private val binding: TrailCardViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindTrail(trail: Trail) = with(binding) {
            trailImage.setImageDrawable(trail.image)
            cardTrailName.text = trail.name
            cardOwnerName.text = trail.owner.username
            totalStarsTextView.text = trail.stars.ordinal.toString()
            difficultyTextView.text = trail.difficulty.toString()
            difficultyTextView.setTextColor(trail.difficulty.color)
            totalReviewsTextView.text = "(${trail.reviews.size})"
            trailPositionDetails.text = trail.getPositionDetails()
            favoriteTrailImageButton.setImageDrawable(
                if (trail.isFavorite) {
                    MainActivity.getDrawable(R.drawable.heart_white_red)
                } else {
                    MainActivity.getDrawable(R.drawable.heart_white_transparent)
                }
            )
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrailCardViewHolder {
        val binding = TrailCardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        val viewHolder = TrailCardViewHolder(binding)

        binding.trailImage.setOnClickListener {
            val position = viewHolder.adapterPosition
            trailCardClickListener(getItem(position))
        }

        binding.favoriteTrailImageButton.setOnClickListener {
            val position = viewHolder.adapterPosition
            favoriteTrailClickListener(getItem(position), it as ImageButton)
        }

        return viewHolder
    }

    override fun onBindViewHolder(trailCardViewHolder: TrailCardViewHolder, position: Int) {
        trailCardViewHolder.bindTrail(getItem(position))
    }

}