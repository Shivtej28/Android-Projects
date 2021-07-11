package com.shivtej.androidprojects.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shivtej.androidprojects.models.LearnBlog
import com.shivtej.androidprojects.utils.SavedPostDao

@Database(entities = [LearnBlog::class], version = 1)
abstract class PostsDatabase : RoomDatabase(){

    abstract fun savedPostDao() : SavedPostDao

    companion object{
        @Volatile
        private var INSTACE: PostsDatabase? = null

        fun getDatabase(context: Context): PostsDatabase{
            val tempInstance = INSTACE
            if(tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PostsDatabase::class.java,
                    "saved_posts"
                ).build()
                INSTACE = instance
                return  instance
            }
        }

    }

}