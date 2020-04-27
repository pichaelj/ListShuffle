package com.pichaelj.listshuffle.ui.lists

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.pichaelj.listshuffle.R
import com.pichaelj.listshuffle.data.AppDatabase
import com.pichaelj.listshuffle.data.ShuffleList
import com.pichaelj.listshuffle.databinding.ListsFragmentBinding
import com.pichaelj.listshuffle.ui.lists.views.AddListListener
import com.pichaelj.listshuffle.ui.lists.views.ExistingListSelectedListener

class ListsFragment : Fragment(), AddListListener, ExistingListSelectedListener {

    private val listsVm: ListsViewModel by viewModels() {
        ListsViewModelFactory(
            requireActivity().application,
            AppDatabase.getInstance(requireContext()).getShuffleListDao())
    }

    private lateinit var binding: ListsFragmentBinding

    private lateinit var adapter: ShuffleListsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        binding = ListsFragmentBinding.inflate(inflater, container, false)
        binding.listsVm = listsVm
        binding.lifecycleOwner = viewLifecycleOwner
        adapter = ShuffleListsAdapter(this, this)
        binding.listRv.adapter = adapter

        binding.listsVm?.apply {
            listDataItems.observe(viewLifecycleOwner, Observer {
                it?.let {
                    adapter.submitList(it)
                }
            })
        }

        initMenuChangeListeners()

        return binding.root
    }

    // region Menu

    private fun initMenuChangeListeners() {
        listsVm.addingListChangedEvent.observe(viewLifecycleOwner, Observer {
            if (it) {
                activity?.invalidateOptionsMenu()
                listsVm.addingListChangedEventHandled()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.shuffle_lists_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val isAdding = listsVm.isAddingList
        menu.findItem(R.id.menu_list_add).isEnabled = !isAdding
        menu.findItem(R.id.menu_list_add).isVisible = !isAdding
        menu.findItem(R.id.menu_list_cancel).isEnabled = isAdding
        menu.findItem(R.id.menu_list_cancel).isVisible = isAdding
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_list_add -> showAddListView()
            R.id.menu_list_cancel -> listsVm.hideAddListView()
        }

        return super.onOptionsItemSelected(item)
    }

    // endregion

    private fun showAddListView() {
        listsVm.showAddListView()
        binding.listRv.smoothScrollToPosition(0)
    }

    // region AddListListener

    override fun onAddList(list: ShuffleList) {
        listsVm.addList(list)

        // Hide keyboard
        //
        val view = activity?.currentFocus
        view?.let { v ->
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    override fun onEmptyListName() {
        Snackbar.make(
            binding.root,
            "Error: List name cannot be empty",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    // region ExistingListSelectedListener

    override fun onListSelected(list: ShuffleList) {
        view?.let {
            it.findNavController().navigate(
                ListsFragmentDirections.actionListsFragmentToItemsFragment(list.id, list.name))
        }
    }

    // endregion
}