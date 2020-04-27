package com.pichaelj.listshuffle.ui.items

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.pichaelj.listshuffle.R
import com.pichaelj.listshuffle.data.AppDatabase
import com.pichaelj.listshuffle.data.ShuffleItem
import com.pichaelj.listshuffle.databinding.ItemsFragmentBinding
import com.pichaelj.listshuffle.ui.items.views.AddItemListener
import com.pichaelj.listshuffle.ui.utils.hideKeyboard
import com.pichaelj.listshuffle.ui.utils.showSnackbarMessage

class ItemsFragment : Fragment(), AddItemListener {

    private val itemsVm: ItemsViewModel by viewModels() {
        ItemsViewModelFactory(
            requireActivity().application,
            AppDatabase.getInstance(requireContext()).getShuffleItemDao(),
            ItemsFragmentArgs.fromBundle(requireArguments())
        )
    }

    private lateinit var binding: ItemsFragmentBinding

    private lateinit var adapter: ShuffleItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        binding = ItemsFragmentBinding.inflate(inflater, container, false)
        binding.itemsVm = itemsVm
        binding.lifecycleOwner = viewLifecycleOwner

        adapter = ShuffleItemsAdapter(this)
        binding.itemsRv.adapter = adapter

        binding.itemsVm?.apply {
            itemDataItems.observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
            })
        }

        initMenuChangedListeners()
        initItemAddedListener()

        val listId = ItemsFragmentArgs.fromBundle(requireArguments()).listId
        Toast.makeText(requireContext(), listId.toString(), Toast.LENGTH_SHORT).show()

        return binding.root
    }

    // region Menu

    private fun initMenuChangedListeners() {
        itemsVm.addingItemChangedEvent.observe(viewLifecycleOwner, Observer {
            if (it) {
                activity?.invalidateOptionsMenu()
                itemsVm.addingItemChangedEventHandled()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.shuffle_lists_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val isAdding = itemsVm.isAddingItem
        menu.findItem(R.id.menu_list_add).isEnabled = !isAdding
        menu.findItem(R.id.menu_list_add).isVisible = !isAdding
        menu.findItem(R.id.menu_list_cancel).isEnabled = isAdding
        menu.findItem(R.id.menu_list_cancel).isVisible = isAdding
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_list_add -> showAddItemView()
            R.id.menu_list_cancel -> itemsVm.hideAddItemView()
        }

        return super.onOptionsItemSelected(item)
    }

    // endregion

    private fun showAddItemView() {
        itemsVm.showAddItemView()
        binding.itemsRv.smoothScrollToPosition(0)
    }

    // region AddItemListener

    override fun onAddItem(item: ShuffleItem) {
        itemsVm.addItem(item)

        hideKeyboard()
    }

    override fun onEmptyItemName() {
        showSnackbarMessage(binding.root, "Error: Item name cannot be empty")
    }

    // endregion

    private fun initItemAddedListener() {
        itemsVm.itemAddedEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                showSnackbarMessage(binding.root, "Added '$it' to list")
                itemsVm.itemAddedEventHandled()
            }
        })
    }

    // endregion
}