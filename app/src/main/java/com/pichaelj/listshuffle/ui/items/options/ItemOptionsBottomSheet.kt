package com.pichaelj.listshuffle.ui.items.options

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pichaelj.listshuffle.data.ShuffleItem
import com.pichaelj.listshuffle.databinding.ItemOptionsBottomSheetBinding

private const val KEY_ITEM_ARG = "item"

interface ItemOptionsListener {
    fun onDelete(item: ShuffleItem)
}

class ItemOptionsBottomSheet : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(item: ShuffleItem): ItemOptionsBottomSheet {
            val args = Bundle()
            args.putSerializable(KEY_ITEM_ARG, item)

            val dialogFrag = ItemOptionsBottomSheet()
            dialogFrag.arguments = args
            return dialogFrag
        }
    }

    private lateinit var listener: ItemOptionsListener

    private lateinit var item: ShuffleItem

    private fun loadItem() {
        arguments?.let { args ->
            if (args.containsKey(KEY_ITEM_ARG)) {
                item = args.getSerializable(KEY_ITEM_ARG) as ShuffleItem
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = targetFragment as ItemOptionsListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadItem()

        val binding = ItemOptionsBottomSheetBinding.inflate(inflater, container, false)

        binding.itemOptionsLabelTv.text = item.label

        binding.listItemCancelIb.setOnClickListener {
            close()
        }

        binding.listItemRmIb.setOnClickListener {
            close()
            listener.onDelete(item)
        }

        return binding.root
    }

    private fun close() {
        dialog?.dismiss()
    }
}