package com.history

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StreamItem(
        val label: String,
        val id: String
) : Parcelable