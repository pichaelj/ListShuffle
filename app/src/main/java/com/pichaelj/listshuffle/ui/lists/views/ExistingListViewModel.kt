package com.pichaelj.listshuffle.ui.lists.views

import com.pichaelj.listshuffle.data.ShuffleList
import timber.log.Timber

interface ExistingListSelectedListener {
    fun onListSelected(list: ShuffleList)
}

class ExistingListViewModel(
    private val listSelectedListener: ExistingListSelectedListener,
    var list: ShuffleList) {

    val listName: String
        get() = list.name

    fun onListClicked() {
        Timber.i("onListClicked(${listName})")
        listSelectedListener?.onListSelected(list)
    }
}