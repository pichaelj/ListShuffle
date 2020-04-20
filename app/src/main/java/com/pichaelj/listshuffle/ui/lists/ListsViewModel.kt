package com.pichaelj.listshuffle.ui.lists

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pichaelj.listshuffle.data.AppDatabase
import com.pichaelj.listshuffle.data.ShuffleList
import com.pichaelj.listshuffle.data.ShuffleListDao
import kotlinx.coroutines.*
import timber.log.Timber

class ListsViewModelFactory(
    private val application: Application,
    private val listDao: ShuffleListDao
) : ViewModelProvider.Factory {

    @SuppressWarnings("unchecked_cast")
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

    init {
        Timber.d("init")
    }

    val allLists: LiveData<List<ShuffleList>> = listsDao.getLists()

    // region List Insertion

    private var listI = 0

    fun insertDummyList() {
        insertList(ShuffleList("Dummy List ${listI++}"))
    }

    private fun insertList(newList: ShuffleList){
        uiScope.launch {
            withContext(Dispatchers.IO) {
                listsDao.insert(newList)
            }
        }
    }

    // endregion


}