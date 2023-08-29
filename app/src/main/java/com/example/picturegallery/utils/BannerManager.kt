package com.example.picturegallery.utils

import android.app.Activity
import android.support.annotation.StringRes
import com.example.picturegallery.R
import com.tapadoo.alerter.Alerter

object BannerManager {

    fun showErrorBanner(
        activity: Activity,
        @StringRes title: Int = R.string.error_text,
        message: String
    ) {
        Alerter.create(activity)
            .enableIconPulse(true)
            .setBackgroundColorRes(R.color.red)
            .setIcon(com.google.android.material.R.drawable.mtrl_ic_error)
            .setTitle(title)
            .setText(message)
            .show()
    }
}