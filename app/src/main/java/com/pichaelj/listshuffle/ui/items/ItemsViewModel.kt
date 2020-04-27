package com.pichaelj.listshuffle.ui.items

import android.app.Application
import androidx.lifecycle.*
import com.pichaelj.listshuffle.data.ShuffleItem
import com.pichaelj.listshuffle.data.ShuffleItemDao
import kotlinx.coroutines.*
import timber.log.Timber

class ItemsViewModelFactory(
    private val app: Application,
    private val itemsDao: ShuffleItemDao,
    private val itemFragArgs: ItemsFragmentArgs) : ViewModelProvider.Factory {

    @SuppressWarnings("Unchecked cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemsViewModel::class.java)) {
            return ItemsViewModel(app, itemsDao, itemFragArgs.listId, itemFragArgs.listName) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ItemsViewModel(
    app: Application,
    private val itemsDao: ShuffleItemDao,
    private val listId: Long,
    private val listName: String
): AndroidViewModel(app) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val allItems: LiveData<List<ShuffleItem>> = itemsDao.getItems(listId)

    // region Inserting Items

    private var _itemAddedEvent = MutableLiveData<String>()

    val itemAddedEvent: LiveData<String>
        get() = _itemAddedEvent

    fun itemAddedEventHandled() {
        _itemAddedEvent.value = null
    }

    private var itemId = 0

    fun insertDummyItem() {
        insertItem(ShuffleItem(listId, "Item ${itemId++}"))
    }

    private fun insertItem(newItem: ShuffleItem){
        uiScope.launch {
            withContext(Dispatchers.IO) {
                itemsDao.insert(newItem)
                _itemAddedEvent.postValue(newItem.label)
            }
        }
    }

    // endregion
}