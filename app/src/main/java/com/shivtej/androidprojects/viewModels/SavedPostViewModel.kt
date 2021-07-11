package com.shivtej.androidprojects.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.shivtej.androidprojects.db.PostsDatabase
import com.shivtej.androidprojects.models.LearnBlog
import com.shivtej.androidprojects.repo.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavedPostViewModel(application: Application) : AndroidViewModel(application) {

    public val readAllPosts: LiveData<List<LearnBlog>>
    private val repository: PostRepository

    init {
        val postDao = PostsDatabase.getDatabase(application).savedPostDao()
        repository = PostRepository(postDao)
        readAllPosts = repository.readPosts
    }

    fun addPost(learnBlog: LearnBlog) {
        viewModelScope.launch {
            repository.addPost(learnBlog)
        }
    }

    fun deletePost(learnBlog: LearnBlog) {
        viewModelScope.launch {
            repository.deletePost(learnBlog)
        }
    }
}