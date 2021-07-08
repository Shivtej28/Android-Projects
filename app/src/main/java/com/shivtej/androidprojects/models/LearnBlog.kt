package com.shivtej.androidprojects.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LearnBlog(
    @SerializedName("title")
    val title : String? = "",
    @SerializedName("description")
    val description : String? = "",
    @SerializedName("url")
   val url : String? = ""
) : Serializable
