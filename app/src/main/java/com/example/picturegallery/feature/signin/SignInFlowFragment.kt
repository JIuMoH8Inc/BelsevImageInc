package com.example.picturegallery.feature.signin

import com.example.picturegallery.R
import com.example.picturegallery.ui.fragment.base.BaseFlowFragment
import com.example.picturegallery.utils.navigation.Route

class SignInFlowFragment : BaseFlowFragment(R.layout.sign_in_flow_fragment, R.id.nav_host_fragment_sign) {
    companion object {
        fun route() = Route(
            destId = R.id.signInFlowFragment
        )
    }
}