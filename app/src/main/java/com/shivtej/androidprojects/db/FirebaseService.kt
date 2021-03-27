package com.shivtej.androidprojects.db

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

import com.shivtej.androidprojects.models.Project
import kotlinx.coroutines.tasks.await

class FirebaseService {

     fun getProjectList(level : String, mycallback : (List<Project>) -> Unit) {

        val db = FirebaseFirestore.getInstance().collection(level)
         db.get().addOnCompleteListener { task ->
             if(task.isSuccessful){
                 val list = ArrayList<Project>()
                 for(document in task.result!!){
                     val project = document.toObject(Project::class.java)
                     list.add(project)
                 }
                 mycallback(list)

             }



         }


    }
}