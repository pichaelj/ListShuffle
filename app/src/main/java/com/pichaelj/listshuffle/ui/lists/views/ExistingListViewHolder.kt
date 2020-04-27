package com.pichaelj.listshuffle.ui.lists.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pichaelj.listshuffle.databinding.ExistingListRvItemBinding

class ExistingListViewHolder private constructor(
    private val binding: ExistingListRvItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): ExistingListViewHolder {
            val binding = ExistingListRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return ExistingListViewHolder(binding)
        }
    }

    fun bind(listItemVm: ExistingListViewModel) {
        binding.existingListVm = listItemVm
    }
}