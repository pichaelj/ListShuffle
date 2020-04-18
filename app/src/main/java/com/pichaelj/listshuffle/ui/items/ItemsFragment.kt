package com.pichaelj.listshuffle.ui.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.pichaelj.listshuffle.databinding.ItemsFragmentBinding

class ItemsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ItemsFragmentBinding.inflate(
            inflater, container, false
        )

        val listId = ItemsFragmentArgs.fromBundle(requireArguments()).listId

        Toast.makeText(requireContext(), listId.toString(), Toast.LENGTH_SHORT).show()

        return binding.root
    }
}