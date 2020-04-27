package com.pichaelj.listshuffle.ui.items.views

import com.pichaelj.listshuffle.data.ShuffleItem

interface AddItemListener {
    fun onAddItem(item: ShuffleItem)

    fun onEmptyItemName()
}

class AddItemViewModel(
    private val addItemListener: AddItemListener,
    var item: ShuffleItem
) {

    fun onDoneButtonClicked() {
        item.label = item.label.trim()

        if (item.label.isEmpty()) {
            addItemListener.onEmptyItemName()
        } else {
            addItemListener.onAddItem(item)
        }
    }
}