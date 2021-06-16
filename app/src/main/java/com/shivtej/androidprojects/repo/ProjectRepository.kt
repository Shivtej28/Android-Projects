package com.shivtej.androidprojects.repo

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProjectRepository {

    val TAG = "Repository"
    val firestoreDB = Firebase.firestore

    fun getBasicProjects() : CollectionReference{
        val collectionReference = firestoreDB.collection("Basic")
        return collectionReference
    }

    fun getIntermediateProjects() : CollectionReference{
        val collectionReference = firestoreDB.collection("Intermediate")
        return collectionReference
    }

    fun getAdvancedProjects() : CollectionReference{
        val collectionReference = firestoreDB.collection("Advance")
        return collectionReference
    }
}