package com.pichaelj.listshuffle.ui.lists

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pichaelj.listshuffle.ui.lists.views.*
import java.lang.IllegalArgumentException

class ShuffleListsAdapter(
    private val addListListener: AddListListener,
    private val listSelectedListener: ExistingListSelectedListener
) : ListAdapter<ShuffleListDataItem, RecyclerView.ViewHolder>(ShuffleListDiffCallback()) {

    override fun submitList(list: List<ShuffleListDataItem>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val listItem = getItem(position)

        when (holder) {
            is AddListViewHolder -> holder.bind(AddListViewModel(addListListener, listItem.list))
            is ExistingListViewHolder -> holder.bind(ExistingListViewModel(listSelectedListener, listItem.list))
            else -> throw IllegalArgumentException("Unknown ViewHolder class")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ShuffleListDataItem.VIEW_TYPE_ADD_LIST -> AddListViewHolder.from(parent)
            ShuffleListDataItem.VIEW_TYPE_EXISTING_LIST -> ExistingListViewHolder.from(parent)
            else -> throw IllegalArgumentException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int =
        getItem(position).viewType
}