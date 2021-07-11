package com.shivtej.androidprojects.utils

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shivtej.androidprojects.models.LearnBlog

@Dao
interface SavedPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(learnBlog: LearnBlog)

    @Query("SELECT * FROM saved_posts ORDER BY id DESC")
    fun readAllPosts() : LiveData<List<LearnBlog>>

    @Delete
    suspend fun deletePost(learnBlog: LearnBlog)
}