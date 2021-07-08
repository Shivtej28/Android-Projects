package com.shivtej.androidprojects.utils

import com.shivtej.androidprojects.models.LearnBlog
import retrofit2.Call
import retrofit2.http.GET

interface Api {

    companion object{
        val BASE_URL: String = "https://dev.to/api/articles?username="

    }

    @GET("articles?username=asadevelopers")
    fun getHeroes(): Call<ArrayList<LearnBlog>>
}