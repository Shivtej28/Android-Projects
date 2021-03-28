package com.shivtej.androidprojects.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Project(
    val title : String = "",
    val description : String = "",
    val apk : String = "",
    val zipfile : String = "",
    val image : String = "") : Parcelable
