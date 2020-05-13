package com.pichaelj.listshuffle.ui.items

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.pichaelj.listshuffle.R
import com.pichaelj.listshuffle.data.AppDatabase
import com.pichaelj.listshuffle.data.ShuffleItem
import com.pichaelj.listshuffle.databinding.ItemsFragmentBinding
import com.pichaelj.listshuffle.ui.items.views.AddItemListener
import com.pichaelj.listshuffle.ui.items.views.ModifyItemListener
import com.pichaelj.listshuffle.ui.items.options.ItemOptionsBottomSheet
import com.pichaelj.listshuffle.ui.items.options.ItemOptionsListener
import com.pichaelj.listshuffle.ui.utils.hideKeyboard
import com.pichaelj.listshuffle.ui.utils.showSnackbarMessage

class ItemsFragment : Fragment(), AddItemListener, ModifyItemListener, ItemOptionsListener {

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

        adapter = ShuffleItemsAdapter(this, this, this)
        binding.itemsRv.adapter = adapter

        binding.itemsVm?.apply {
            itemDataItems.observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
            })
        }

        initMenuChangedObserver()
        initItemAddedObserver()
        initItemDeletedObserver()

        val listId = ItemsFragmentArgs.fromBundle(requireArguments()).listId
        Toast.makeText(requireContext(), listId.toString(), Toast.LENGTH_SHORT).show()

        return binding.root
    }

    // region Menu

    private fun initMenuChangedObserver() {
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

    private fun showAddItemView() {
        itemsVm.showAddItemView()
        binding.itemsRv.smoothScrollToPosition(0)
    }

    // endregion

    // region ItemViewModel Event Listeners

    private fun initItemAddedObserver() {
        itemsVm.itemAddedEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                showSnackbarMessage(binding.root, "Added '$it' to list")
                itemsVm.itemAddedEventHandled()
            }
        })
    }

    override fun onAddItem(item: ShuffleItem) {
        itemsVm.addItem(item)

        hideKeyboard()
    }

    override fun onEmptyItemName() {
        showSnackbarMessage(binding.root, "Error: Item name cannot be empty")
    }

    private fun initItemDeletedObserver() {
        itemsVm.itemDeletedEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                showSnackbarMessage(binding.root, "Deleted '$it'")
            }
        })
    }

    // endregion

    // region Edit Item Listeners

    override fun onMondifyItem(item: ShuffleItem) {
        // Show requested item in an options bottom sheet
        //
        var fm = parentFragmentManager
        if (fm != null) {
            var bottomSheet = ItemOptionsBottomSheet.newInstance(item)
            bottomSheet.setTargetFragment(this, 0)
            bottomSheet.show(fm, "ModalBottomSheet")
        }
    }

    // endregion

    // region ItemOptionsListener implementation

    override fun onDelete(item: ShuffleItem) {
        itemsVm.deleteItem(item)
    }

    // endregion
}