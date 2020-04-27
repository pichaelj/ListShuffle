package com.pichaelj.listshuffle.ui.lists

import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.BindingAdapter

interface OnOkayInSoftKeyboardListener {
    fun onOkay(v: View)
}

@BindingAdapter("onOkayInSoftKeyboard")
fun EditText.onOkayInSoftKeyboard(listener: () -> Unit) {
    if (listener == null) {
        setOnEditorActionListener(null)
    } else {
        setOnEditorActionListener { v, actionId, event ->
            val okayImeAction = when (actionId) {
                EditorInfo.IME_ACTION_DONE,
                EditorInfo.IME_ACTION_SEND,
                EditorInfo.IME_ACTION_GO -> true
                else -> false
            }

            val okayKeyDownEvent = event?.keyCode == KeyEvent.KEYCODE_ENTER
                    && event.action == KeyEvent.ACTION_DOWN

            if (okayImeAction || okayKeyDownEvent) {
                true.also { listener() }
            } else false
        }
    }
}