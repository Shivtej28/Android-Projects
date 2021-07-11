package com.shivtej.androidprojects.repo

import androidx.lifecycle.LiveData
import com.shivtej.androidprojects.models.LearnBlog
import com.shivtej.androidprojects.utils.SavedPostDao

class PostRepository(private val postDao: SavedPostDao) {

    val readPosts: LiveData<List<LearnBlog>> = postDao.readAllPosts()

    suspend fun addPost(learnBlog: LearnBlog) {
        postDao.insertPost(learnBlog)
    }

    suspend fun deletePost(learnBlog: LearnBlog){
        postDao.deletePost(learnBlog)
    }

}