package com.shivtej.androidprojects.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProjectList(
    val title : String = "",
    val description : String = "",
    val apk : String = "",
    val zipfile : String = "",
    val image : String = "",
    val ss1 : String = "",
    val ss2 : String = "",
    val ss3 : String = "",
    val ss4 : String = "",
    val ss5 : String = "")  : Parcelable
