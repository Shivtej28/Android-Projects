package com.shivtej.androidprojects.models

import java.io.Serializable

data class LearnBlog(
    val title : String? = "",
    val detail : String? = "",
    val url : String? = ""
) : Serializable
