package com.example.natour.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.natour.data.model.Trail
import com.example.natour.databinding.CardViewBinding

class RouteItemAdapter(
    private val inflater: LayoutInflater,
    private val dataset: List<Trail>
): RecyclerView.Adapter<RouteItemAdapter.CardViewHolder>() {

    class CardViewHolder(private val binding: CardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val cardView: LinearLayout = binding.cardView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = CardViewHolder(CardViewBinding.inflate(inflater, parent, false))

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = dataset[position]
    }

    override fun getItemCount() = dataset.size
}