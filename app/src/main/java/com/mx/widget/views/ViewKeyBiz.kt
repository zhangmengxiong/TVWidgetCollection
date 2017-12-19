package com.mx.widget.views

import android.view.KeyEvent
import android.view.View

/**
 * Created by ZMX on 2017/12/12.
 */
fun View.setOnKey(action: (keyCode: Int) -> Boolean) {
    this.setOnKeyListener { v, keyCode, event ->
        var result = false
        if (event.action == KeyEvent.ACTION_DOWN
                && keyCode in arrayOf(KeyEvent.KEYCODE_DPAD_RIGHT,
                KeyEvent.KEYCODE_DPAD_DOWN,
                KeyEvent.KEYCODE_DPAD_LEFT,
                KeyEvent.KEYCODE_DPAD_UP)) {
            result = action(keyCode)
        }
        result
    }
}