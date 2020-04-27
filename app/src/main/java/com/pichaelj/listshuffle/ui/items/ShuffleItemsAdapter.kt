package com.pichaelj.listshuffle.ui.items

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pichaelj.listshuffle.ui.items.views.*
import java.lang.IllegalArgumentException

class ShuffleItemsAdapter(
    private val addItemListener: AddItemListener
) : ListAdapter<ShuffleItemDataItem, RecyclerView.ViewHolder>(ShuffleItemDiffCallback()) {

    override fun submitList(list: List<ShuffleItemDataItem>?){
        super.submitList(list?.let { ArrayList(it) })
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is AddItemViewHolder -> holder.bind(AddItemViewModel(addItemListener, item.item))
            is ExistingItemViewHolder -> holder.bind(ExistingItemViewModel(item.item))
            else -> throw IllegalArgumentException("Unknown ViewHolder class")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            ShuffleItemDataItem.VIEW_TYPE_ADD_ITEM -> AddItemViewHolder.from(parent)
            ShuffleItemDataItem.VIEW_TYPE_EXISTING_ITEM -> ExistingItemViewHolder.from(parent)
            else -> throw IllegalArgumentException("Unknown viewType $viewType")
        }

    override fun getItemViewType(position: Int): Int =
        getItem(position).viewType
}