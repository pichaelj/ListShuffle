package com.pichaelj.listshuffle.ui.items

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.pichaelj.listshuffle.R
import com.pichaelj.listshuffle.data.AppDatabase
import com.pichaelj.listshuffle.databinding.ItemsFragmentBinding

class ItemsFragment : Fragment() {

    private val itemsVm: ItemsViewModel by viewModels() {
        ItemsViewModelFactory(
            requireActivity().application,
            AppDatabase.getInstance(requireContext()).getShuffleItemDao(),
            ItemsFragmentArgs.fromBundle(requireArguments())
        )
    }

    private lateinit var adapter: ShuffleItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val binding = ItemsFragmentBinding.inflate(inflater, container, false)
        binding.itemsVm = itemsVm
        binding.lifecycleOwner = viewLifecycleOwner

        adapter = ShuffleItemsAdapter()
        binding.itemsRv.adapter = adapter
        binding.itemsVm?.apply {
            allItems.observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
            })
        }

        initItemAddedListener()

        val listId = ItemsFragmentArgs.fromBundle(requireArguments()).listId
        Toast.makeText(requireContext(), listId.toString(), Toast.LENGTH_SHORT).show()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.shuffle_lists_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_list_add -> addNewItem()
        }

        return super.onOptionsItemSelected(item)
    }

    // region Adding Items

    private fun initItemAddedListener() {
        itemsVm.itemAddedEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Toast.makeText(context, "Added '$it' to list", Toast.LENGTH_SHORT).show()
                itemsVm.itemAddedEventHandled()
            }
        })
    }

    private fun addNewItem() {
        itemsVm.insertDummyItem()
    }

    // endregion
}