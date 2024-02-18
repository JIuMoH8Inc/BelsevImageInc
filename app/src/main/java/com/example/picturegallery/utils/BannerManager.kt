package com.example.picturegallery.utils

import android.app.Activity
import androidx.annotation.StringRes
import com.example.picturegallery.R
import com.tapadoo.alerter.Alerter

object BannerManager {

    fun showErrorBanner(
        activity: Activity,
        @StringRes title: Int = R.string.error_text,
        message: String
    ) =
        Alerter.create(activity)
            .enableIconPulse(true)
            .setBackgroundColorRes(R.color.red)
            .setIcon(R.drawable.ic_error)
            .setTitle(title)
            .setText(message)
            .show()

    fun showSuccessBanner(
        activity: Activity,
        @StringRes title: Int = R.string.success,
        message: String
    ) = Alerter.create(activity)
        .enableIconPulse(true)
        .setBackgroundColorRes(R.color.green)
        .setIcon(R.drawable.ic_check_success)
        .setTitle(title)
        .setText(message)
        .show()
}