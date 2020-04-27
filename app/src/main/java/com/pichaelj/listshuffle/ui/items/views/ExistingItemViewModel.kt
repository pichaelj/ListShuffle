package com.pichaelj.listshuffle.ui.items.views

import com.pichaelj.listshuffle.data.ShuffleItem

class ExistingItemViewModel(var item: ShuffleItem) {

    val itemName: String
        get() = item.label
}