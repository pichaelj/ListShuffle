package com.pichaelj.listshuffle.ui.lists.views

import com.pichaelj.listshuffle.data.ShuffleList

data class ShuffleListDataItem private constructor(val viewType: Int, var list: ShuffleList) {

    val isAddingView: Boolean
        get() = viewType == VIEW_TYPE_ADD_LIST

    companion object {

        const val VIEW_TYPE_ADD_LIST = 0
        const val VIEW_TYPE_EXISTING_LIST = 1

        fun newAdding() : ShuffleListDataItem{
            return ShuffleListDataItem(VIEW_TYPE_ADD_LIST, ShuffleList(""))
        }

        fun newExisting(list: ShuffleList) : ShuffleListDataItem{
            return ShuffleListDataItem(VIEW_TYPE_EXISTING_LIST, list)
        }
    }
}