package com.example.picturegallery.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.example.picturegallery.R
import com.example.picturegallery.ui.activity.AppActivity
import com.example.picturegallery.utils.extensions.activityNavController
import com.example.picturegallery.utils.extensions.navigateSafely

class SplashFragment : Fragment(R.layout.splash_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.view?.postDelayed(
            {
                (activity as AppActivity).test.setKeepOnScreenCondition { false }
                activityNavController().navigateSafely(R.id.action_splashFlowFragment_to_signInFlowFragment)
            },
            3000L
        )

    }

}