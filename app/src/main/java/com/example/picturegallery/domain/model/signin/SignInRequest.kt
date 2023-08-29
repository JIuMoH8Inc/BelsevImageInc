package com.example.picturegallery.domain.model.signin

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SignInRequest(
    val userName: String = "",
    val password: String  = "",
    val keepLoggedIn: String = "off"
) : Parcelable
