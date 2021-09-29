package com.taked.stamp.viewmodel.util

import android.content.Context
import android.view.Gravity
import android.widget.Toast

object ToastUtil {
    fun makeBottomToast(context: Context, message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        // 位置調整
//        toast.setGravity(Gravity., 0, 0)
        toast.show()
    }
}
