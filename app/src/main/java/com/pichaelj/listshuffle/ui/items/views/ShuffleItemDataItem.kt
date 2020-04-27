package com.pichaelj.listshuffle.ui.items.views

import com.pichaelj.listshuffle.data.ShuffleItem

class ShuffleItemDataItem private constructor(val viewType: Int, var item: ShuffleItem) {

    val isAddingView: Boolean
        get() = viewType == VIEW_TYPE_ADD_ITEM

    companion object {

        const val VIEW_TYPE_ADD_ITEM = 0
        const val VIEW_TYPE_EXISTING_ITEM = 1

        fun newAdding(listId: Long): ShuffleItemDataItem {
            return ShuffleItemDataItem(VIEW_TYPE_ADD_ITEM, ShuffleItem(listId, ""))
        }

        fun newExisting(item: ShuffleItem): ShuffleItemDataItem {
            return ShuffleItemDataItem(VIEW_TYPE_EXISTING_ITEM, item)
        }
    }
}