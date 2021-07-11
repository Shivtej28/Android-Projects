package com.shivtej.androidprojects.utils

import com.shivtej.androidprojects.models.LearnBlog
import retrofit2.Call
import retrofit2.http.GET

interface Api {

    companion object {
        const val BASE_URL: String = "https://dev.to/api/"

    }

    @GET("articles?username=asadevelopers")
    fun getHeroes(): Call<ArrayList<LearnBlog>>
}