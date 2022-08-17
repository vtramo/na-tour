package com.example.natour.ui.home.trail.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.natour.data.model.TrailReview
import com.example.natour.databinding.TrailReviewViewBinding

class TrailReviewListAdapter:
    ListAdapter<TrailReview, TrailReviewListAdapter.TrailReviewViewHolder>(TrailDiffCallback) {

    companion object {
        private val TrailDiffCallback = object : DiffUtil.ItemCallback<TrailReview>() {
            override fun areItemsTheSame(oldItem: TrailReview, newItem: TrailReview): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: TrailReview, newItem: TrailReview): Boolean =
                oldItem == newItem
        }
    }

    inner class TrailReviewViewHolder(
        private val binding: TrailReviewViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindTrailReview(trailReview: TrailReview) = with(binding) {
            trailReviewStarsImageView.setImageDrawable(trailReview.getStarsImage())
            trailReviewDescriptionTextView.text = trailReview.description
            trailReviewUsernameOwnerTextView.text = trailReview.owner.username
            trailReviewDateTextView.text = trailReview.date
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrailReviewViewHolder = TrailReviewViewHolder(
        TrailReviewViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(trailReviewViewHolder: TrailReviewViewHolder, position: Int) {
        trailReviewViewHolder.bindTrailReview(getItem(position))
    }
}