package com.pichaelj.listshuffle.ui.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.pichaelj.listshuffle.data.AppDatabase
import com.pichaelj.listshuffle.data.ShuffleList
import com.pichaelj.listshuffle.databinding.ListFragmentBinding
import timber.log.Timber

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
        val binding = ListFragmentBinding.inflate(inflater, container, false)
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
        Timber.i("onListSelected")
        Toast.makeText(requireContext(), list.name, Toast.LENGTH_SHORT)
            .show()
    }
}