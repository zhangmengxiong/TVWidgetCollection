package com.mx.widget.utils

import android.view.KeyEvent
import android.view.View

/**
 * 内联方法定义类
 * Created by ZMX on 2017/12/7.
 */
val Any.TAG: String
    get() = this::class.java.simpleName

inline fun <reified T> T.Log(log: Any) {
    android.util.Log.v(T::class.java.simpleName, log.toString())
}

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