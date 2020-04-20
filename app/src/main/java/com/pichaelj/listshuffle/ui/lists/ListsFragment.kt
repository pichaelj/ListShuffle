package com.pichaelj.listshuffle.ui.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.pichaelj.listshuffle.R
import com.pichaelj.listshuffle.data.AppDatabase
import com.pichaelj.listshuffle.data.ShuffleList
import com.pichaelj.listshuffle.databinding.ListsFragmentBinding

class ListsFragment : Fragment(), ShuffleListSelectedListener {

    private val listsVm: ListsViewModel by viewModels() {
        ListsViewModelFactory(
            requireActivity().application,
            AppDatabase.getInstance(requireContext()).getShuffleListDao())
    }

    private lateinit var adapter: ShuffleListsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val binding = ListsFragmentBinding.inflate(inflater, container, false)
        binding.listsVm = listsVm
        binding.lifecycleOwner = this
        adapter = ShuffleListsAdapter(this)
        binding.listRv.adapter = adapter

        binding.listsVm?.apply {
            allLists?.observe(viewLifecycleOwner, Observer {
                it?.let {
                    adapter.submitList(it)
                }
            })
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.shuffle_lists_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_list_add -> launchNewItemDialog()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun launchNewItemDialog() {
        Toast.makeText(context, "Added new list", Toast.LENGTH_SHORT).show()
        listsVm.insertDummyList()
    }

    override fun onListSelected(list: ShuffleList) {
        view?.let {
            it.findNavController().navigate(
                ListsFragmentDirections.actionListsFragmentToItemsFragment(list.id))
        }
    }
}