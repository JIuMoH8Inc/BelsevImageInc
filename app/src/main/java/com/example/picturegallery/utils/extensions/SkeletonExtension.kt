package com.example.picturegallery.utils.extensions

import com.faltenreich.skeletonlayout.Skeleton

fun Skeleton.setLoadingState(isLoading: Boolean) {
    if (isLoading) showSkeleton() else showOriginal()
}