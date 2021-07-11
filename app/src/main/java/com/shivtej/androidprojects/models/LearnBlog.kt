package com.shivtej.androidprojects.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "saved_posts")
data class LearnBlog(
    @SerializedName("id")
    @PrimaryKey
    val id: Int,
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("description")
    val description: String? = "",
    @SerializedName("url")
    val url: String? = "",
    //var isSaved: Boolean = false
) : Serializable
