package com.pichaelj.listshuffle.ui.lists

import android.app.Application
import androidx.lifecycle.*
import com.pichaelj.listshuffle.data.ShuffleList
import com.pichaelj.listshuffle.data.ShuffleListDao
import com.pichaelj.listshuffle.ui.lists.views.ShuffleListDataItem
import com.pichaelj.listshuffle.ui.utils.CombinedLiveData
import kotlinx.coroutines.*
import timber.log.Timber

class ListsViewModelFactory(
    private val application: Application,
    private val listDao: ShuffleListDao
) : ViewModelProvider.Factory {

    @SuppressWarnings("Unchecked cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListsViewModel::class.java)) {
            return ListsViewModel(application, listDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ListsViewModel(
    application: Application,
    private val listsDao: ShuffleListDao
) : AndroidViewModel(application) {

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // region Add List

    private var _isAddingList = MutableLiveData<Boolean>(false)

    val isAddingList: Boolean
        get() = _isAddingList.value!!

    fun showAddListView() {
        _isAddingList.value = true
        setAddingListChangedEvent()
    }

    fun hideAddListView() {
        _isAddingList.value = false
        setAddingListChangedEvent()
    }

    // endregion

    // region Adding List Changed Event

    private var _addingListChangedEvent = MutableLiveData<Boolean>(false)

    private fun setAddingListChangedEvent() {
        _addingListChangedEvent.value = true
    }

    val addingListChangedEvent: LiveData<Boolean>
        get() = _addingListChangedEvent

    fun addingListChangedEventHandled() {
        _addingListChangedEvent.value = false
    }

    // endregion

    // region Existing Lists

    private var _lists: LiveData<List<ShuffleList>> = listsDao.getLists()

    private val _existingLists: LiveData<MutableList<ShuffleListDataItem>>
        get() = Transformations.map(_lists) { lists ->
            lists.map { ShuffleListDataItem.newExisting(it) }.toMutableList()
        }

    // endregion

    // region Joining Add + Existing List Data Items

    val listDataItems = CombinedLiveData<Boolean, MutableList<ShuffleListDataItem>, List<ShuffleListDataItem>>(
        _isAddingList,
        _existingLists,
        this::handleAddingItem
    )

    private fun handleAddingItem(
        isAddingList: Boolean?,
        listVms: MutableList<ShuffleListDataItem>?
    ) : List<ShuffleListDataItem> {
        if (listVms == null) {
            return listOf()
        }

        if (isAddingList == null) {
            return listVms
        }

        // Check and see if the existing lists already contains the "Add Data Item"
        val listContainsAddView = if (listVms.size > 0) listVms[0].isAddingView else false

        // Add/remove the "Add Data Item" if needed
        if (isAddingList && !listContainsAddView) {
            listVms.add(0,
                ShuffleListDataItem.newAdding()
            )
        } else if (!isAddingList && listContainsAddView) {
            listVms.removeAt(0)
        }

        return listVms
    }

    // endregion

    // region List Creation

    fun addList(list: ShuffleList){
        insertList(list)
        hideAddListView()
    }

    private fun insertList(newList: ShuffleList){
        uiScope.launch {
            withContext(Dispatchers.IO) {
                listsDao.insert(newList)
                postListCreatedEvent(newList.name)
            }
        }
    }

    // region List Created Event

    private var _listCreatedEvent = MutableLiveData<String>()

    val listCreatedEvent: LiveData<String>
        get() = _listCreatedEvent

    private fun postListCreatedEvent(listName: String) {
        _listCreatedEvent.postValue(listName)
    }

    fun listCreatedEventHandled() {
        _listCreatedEvent.value = null
    }

    // endregion

    // endregion
}