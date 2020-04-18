package com.pichaelj.listshuffle.ui.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onListSelected(list: ShuffleList) {
        view?.let {
            it.findNavController().navigate(
                ListsFragmentDirections.actionListsFragmentToItemsFragment(list.id))
        }
    }
}