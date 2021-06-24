package com.shivtej.androidprojects.repo

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shivtej.androidprojects.models.User

class ProjectRepository {

    val TAG = "Repository"
    val firestoreDB = Firebase.firestore
    var user: User? = null

    fun getBasicProjects(): CollectionReference {
        val collectionReference = firestoreDB.collection("Basic")
        return collectionReference
    }

    fun getIntermediateProjects(): CollectionReference {
        val collectionReference = firestoreDB.collection("Intermediate")
        return collectionReference
    }

    fun getAdvancedProjects(): CollectionReference {
        val collectionReference = firestoreDB.collection("Advance")
        return collectionReference
    }

    fun getQuizReference(quizName: String): CollectionReference {
        return firestoreDB.collection(quizName)
    }

    fun getUserReference(uid: String): DocumentReference {
        return firestoreDB.collection("User").document(uid)
    }

    fun addUserToFirebase(user: User) {
        val reference = user.uid?.let { getUserReference(it) }
        Log.i(TAG, reference?.path.toString())
        if (reference != null) {
            reference.set(user)
                .addOnSuccessListener {
                    Log.i(TAG, "Success")
                }
                .addOnFailureListener {
                    Log.i(TAG, "Fail : ${it.message}")
                }
        }
    }

    fun getLearnReference() : CollectionReference{
        return firestoreDB.collection("Learn")
    }


}