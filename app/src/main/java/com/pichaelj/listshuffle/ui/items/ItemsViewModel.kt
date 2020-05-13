package com.pichaelj.listshuffle.ui.items

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import com.pichaelj.listshuffle.data.ShuffleItem
import com.pichaelj.listshuffle.data.ShuffleItemDao
import com.pichaelj.listshuffle.ui.items.views.ShuffleItemDataItem
import com.pichaelj.listshuffle.ui.utils.CombinedLiveData
import kotlinx.coroutines.*
import timber.log.Timber

class ItemsViewModelFactory(
    private val app: Application,
    private val itemsDao: ShuffleItemDao,
    private val itemFragArgs: ItemsFragmentArgs
) : ViewModelProvider.Factory {

    @SuppressWarnings("unchecked")
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
) : AndroidViewModel(app) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // region Add Item

    private var _isAddingItem = MutableLiveData<Boolean>(false)

    val isAddingItem: Boolean
        get() = _isAddingItem.value!!

    fun showAddItemView() {
        _isAddingItem.value = true
        setAddingItemChangedEvent()
    }

    fun hideAddItemView() {
        _isAddingItem.value = false
        setAddingItemChangedEvent()
    }

    // endregion

    // region Existing Items

    private var _existingDbItems: LiveData<List<ShuffleItem>> = itemsDao.getItems(listId)

    private val _existingItems: LiveData<MutableList<ShuffleItemDataItem>>
        get() = Transformations.map(_existingDbItems) {
            it.map { ShuffleItemDataItem.newExisting(it) }.toMutableList()
        }

    // endregion

    // region Joining Add + Existing Data Items

    val itemDataItems =
        CombinedLiveData<Boolean, MutableList<ShuffleItemDataItem>, List<ShuffleItemDataItem>>(
            _isAddingItem,
            _existingItems,
            this::handleAddingItem
        )

    private fun handleAddingItem(
        isAddingItem: Boolean?,
        existingItems: MutableList<ShuffleItemDataItem>?
    ): List<ShuffleItemDataItem> {
        if (existingItems == null) {
            return listOf()
        }

        if (isAddingItem == null) {
            return existingItems
        }

        // Check and see if list already contains the "Add Data Item"
        val existingItemsContainsAddView =
            if (existingItems.size > 0) existingItems[0].isAddingView else false

        // Add or remove the "Add Data Item" if needed
        if (isAddingItem && !existingItemsContainsAddView) {
            existingItems.add(0, ShuffleItemDataItem.newAdding(listId))
        } else if (!isAddingItem && existingItemsContainsAddView) {
            existingItems.removeAt(0)
        }

        return existingItems
    }

    // endregion

    // region Adding Item Changed Event

    private var _addingItemChangedEvent = MutableLiveData<Boolean>(false)

    private fun setAddingItemChangedEvent() {
        _addingItemChangedEvent.value = true
    }

    val addingItemChangedEvent: LiveData<Boolean>
        get() = _addingItemChangedEvent

    fun addingItemChangedEventHandled() {
        _addingItemChangedEvent.value = false
    }

    // endregion

    // region Item Creation

    fun addItem(item: ShuffleItem) {
        insertItemAsync(item)
        hideAddItemView()
    }

    private fun insertItemAsync(newItem: ShuffleItem) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                itemsDao.insert(newItem)
                postItemAddedEvent(newItem.label)
            }
        }
    }

    // region Item Added Event

    private var _itemAddedEvent = MutableLiveData<String>()

    val itemAddedEvent: LiveData<String>
        get() = _itemAddedEvent

    private fun postItemAddedEvent(itemName: String) {
        _itemAddedEvent.postValue(itemName)
    }

    fun itemAddedEventHandled() {
        _itemAddedEvent.value = null
    }

    // endregion

    // endregion

    // region Item Deletion

    fun deleteItem(item: ShuffleItem) {
        deleteItemAsync(item)
    }

    private fun deleteItemAsync(item: ShuffleItem) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                itemsDao.delete(item)
                postItemDeletedEvent(item.label)
            }
        }
    }

    // region Item Deleted Event

    private var _itemDeletedEvent = MutableLiveData<String>()

    val itemDeletedEvent: LiveData<String>
        get() = _itemDeletedEvent

    private fun postItemDeletedEvent(itemName: String) {
        _itemDeletedEvent.postValue(itemName)
    }

    fun itemDeletedEventHandled() {
        _itemDeletedEvent.value = null
    }

    // endregion

    // endregion
}