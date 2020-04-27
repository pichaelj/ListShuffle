package com.pichaelj.listshuffle.ui.lists.views

import com.pichaelj.listshuffle.data.ShuffleList

interface AddListListener {
    fun onAddList(list: ShuffleList)

    fun onEmptyListName()
}

class AddListViewModel(
    private val addListListener: AddListListener,
    var list: ShuffleList
) {

    fun onDoneButtonClicked() {
        list.name = list.name.trim()

        if (list.name.isEmpty()) {
            addListListener.onEmptyListName()
        } else {
            addListListener.onAddList(list)
        }
    }
}