package com.pichaelj.listshuffle.ui.items.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pichaelj.listshuffle.databinding.AddItemRvItemBinding

class AddItemViewHolder private constructor(
    private val binding: AddItemRvItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): AddItemViewHolder {
            val binding = AddItemRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return AddItemViewHolder(binding)
        }
    }

    fun bind(addItemVm: AddItemViewModel) {
        binding.addItemVm = addItemVm
    }
}