package com.pichaelj.listshuffle.ui.items.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pichaelj.listshuffle.databinding.ExistingItemRvItemBinding

class ExistingItemViewHolder private constructor(
    private val binding: ExistingItemRvItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): ExistingItemViewHolder {
            val binding = ExistingItemRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return ExistingItemViewHolder(binding)
        }
    }

    fun bind(itemVm: ExistingItemViewModel) {
        binding.existingItemVm = itemVm
        binding.root.setOnClickListener(itemVm)
    }
}