package com.pichaelj.listshuffle.ui.items

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pichaelj.listshuffle.data.ShuffleItemDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ItemsViewModelFactory(
    private val app: Application,
    private val itemsDao: ShuffleItemDao,
    private val listId: Long) : ViewModelProvider.Factory {

    @SuppressWarnings("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemsViewModelFactory::class.java)) {
            return ItemsViewModel(app, itemsDao, listId) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ItemsViewModel(
    app: Application,
    private val itemsDao: ShuffleItemDao,
    val listId: Long
): AndroidViewModel(app) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


}