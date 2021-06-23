package com.shivtej.androidprojects.models

data class User(
    val uid: String? = "",
    val userName: String? = "",
    val email: String? = "",
    var coins: Int? = 0
)
