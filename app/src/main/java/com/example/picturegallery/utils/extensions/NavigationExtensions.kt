package com.example.picturegallery.utils.extensions

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.picturegallery.R

fun Fragment.activityNavController() = requireActivity().findNavController(R.id.nav_host_fragment)