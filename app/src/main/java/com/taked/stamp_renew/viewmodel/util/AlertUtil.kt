package com.taked.stamp_renew.viewmodel.util

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.os.CountDownTimer
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.taked.stamp_renew.databinding.CustomDialogBinding

class AlertUtil {
    companion object {
        private lateinit var timer: CountDownTimer
        private var result = false

        private fun getTimer(progressBar: ProgressBar, dialog: AlertDialog) =
            object : CountDownTimer(4500, 1000) {
                var count = -1
                override fun onTick(millisUntilFinished: Long) {
                    count++
                    if (0 < count) {
                        printProgress(count * 100 / 3, progressBar)
                    }
                }

                override fun onFinish() {
                    dialog.dismiss()
                    result = true
                    Looper.myLooper()!!.quit()
                }
            }

        private fun printProgress(percentage: Int, progressBar: ProgressBar) {
            ObjectAnimator.ofInt(progressBar, "progress", percentage).apply {
                duration = 800
                interpolator = DecelerateInterpolator()
                start()
            }
        }

        private fun launchAlertDialog(
            context: Context, message: String, container: ViewGroup?
        ): Boolean {
            val customLayout =
                CustomDialogBinding.inflate(LayoutInflater.from(context), container, false).apply {
                    progressName = message
                }
            val builder = AlertDialog.Builder(context).apply {
                setNegativeButton("Cancel") { dialog, _ ->
                    timer.cancel()
                    dialog.cancel()
                    Looper.myLooper()!!.quit()
                }
                setView(customLayout.root)
            }

            val progressBar = customLayout.progressBar.apply {
                printProgress(0, this)
            }
            val dialog = builder.create().apply {
                setCanceledOnTouchOutside(false)
            }

            timer = getTimer(progressBar, dialog).run {
                start()
            }

            dialog.show()

            result = false
            try {
                Looper.loop()
            } catch (e: RuntimeException) {
            }

            return result
        }

        fun showProgressDialog(context: Context, message: String, container: ViewGroup?): Boolean =
            launchAlertDialog(context, message, container)

        fun showNotifyDialog(
            activity: Activity, title: String = "", message: String = "",
            cancelFlag: Boolean = false, callback: () -> Unit = {}
        ) {
            if (activity.isFinishing || activity.isDestroyed) {
                return
            }

            AlertDialog.Builder(activity).apply {
                if (title.isNotEmpty()) {
                    setTitle(title)
                }
                if (message.isNotEmpty()) {
                    setMessage(message)
                }
                setPositiveButton("OK") { _, _ ->
                    callback()
                }
                if (cancelFlag) {
                    setNegativeButton("cancel") { _, _ ->
                    }
                }
                setCancelable(false)
            }.show()

        }

    }
}