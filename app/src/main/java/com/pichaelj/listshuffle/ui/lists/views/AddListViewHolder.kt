package com.pichaelj.listshuffle.ui.lists.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pichaelj.listshuffle.databinding.AddListRvItemBinding

class AddListViewHolder private constructor(
    private val binding: AddListRvItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): AddListViewHolder {
            val binding = AddListRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return AddListViewHolder(binding)
        }
    }

    fun bind(addListVm: AddListViewModel) {
        binding.addListVm = addListVm
    }
}