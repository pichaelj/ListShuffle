package com.pichaelj.listshuffle.ui.items.views

import android.view.View
import com.pichaelj.listshuffle.data.ShuffleItem

interface ModifyItemListener {
    fun onMondifyItem(item: ShuffleItem)
}

class ExistingItemViewModel (
    private val modifyItemListener: ModifyItemListener,
    var item: ShuffleItem
): View.OnClickListener {

    val itemName: String
        get() = item.label

    override fun onClick(v: View?) {
        modifyItemListener.onMondifyItem(item)
    }
}