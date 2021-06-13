package com.shivtej.androidprojects.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable


data class Project(
    val title : String = "",
    val description : String = "",
    val apk : String = "",
    val zipfile : String = "",
    val image : String = "",
    val ss1 : String = "",
    val ss2 : String = "",
    val ss3 : String = "",
    val ss4 : String = "",
    val ss5 : String = "",
    val ss6 : String = "",
    val ss7 : String = "",
    val ss8 : String = "",
    val ss9 : String = "",
)  : Serializable
