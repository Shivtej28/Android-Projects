package com.shivtej.androidprojects.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.shivtej.androidprojects.models.LearnBlog
import com.shivtej.androidprojects.repo.ProjectRepository
import com.shivtej.androidprojects.models.Project
import com.shivtej.androidprojects.models.Question
import com.shivtej.androidprojects.models.User
import kotlinx.coroutines.launch

class ProjectViewModel : ViewModel() {

    val TAG = "ViewModel"

    var basicProjectList: MutableLiveData<List<Project>> = MutableLiveData()
    var intermediateProjectList: MutableLiveData<List<Project>> = MutableLiveData()
    var advanceProjectList: MutableLiveData<List<Project>> = MutableLiveData()
    val quizList: MutableLiveData<List<Question>> = MutableLiveData()
    val learnBlogList : MutableLiveData<List<LearnBlog>> = MutableLiveData()
    val repository = ProjectRepository()
    var user: User? = null


    fun getQuestions(quizName: String): LiveData<List<Question>> {
        getQuizQuestion(quizName)
        return quizList
    }

    fun getBasicProjects(): LiveData<List<Project>> {
        getBProjects()
        Log.i("list", basicProjectList.toString())
        return basicProjectList
    }

    fun getIntermediateProjects(): LiveData<List<Project>> {
        getIProjects()
        Log.i("list", intermediateProjectList.toString())
        return intermediateProjectList
    }

    fun getAdvanceProjects(): LiveData<List<Project>> {
        getAProjects()
        Log.i("list", advanceProjectList.toString())
        return advanceProjectList
    }

    private fun getAProjects() {
        repository.getAdvancedProjects()
            .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.w(TAG, "Failed", e)
                    advanceProjectList.value = null
                    return@EventListener
                }
                val list: MutableList<Project> = mutableListOf()
                for (doc in value!!) {
                    val project = doc.toObject(Project::class.java)
                    list.add(project)
                }

                advanceProjectList.value = list
                Log.i("List", basicProjectList.value.toString())
            })
    }

    fun getBProjects() {
        repository.getBasicProjects()
            .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.w(TAG, "Failed", e)
                    basicProjectList.value = null
                    return@EventListener
                }
                val basicList: MutableList<Project> = mutableListOf()
                for (doc in value!!) {
                    val project = doc.toObject(Project::class.java)
                    basicList.add(project)
                }

                basicProjectList.value = basicList
                Log.i("List", basicProjectList.value.toString())
            })
    }

    fun getIProjects() {
        repository.getIntermediateProjects()
            .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.w(TAG, "Failed", e)
                    intermediateProjectList.value = null
                    return@EventListener
                }
                val list: MutableList<Project> = mutableListOf()
                for (doc in value!!) {
                    val project = doc.toObject(Project::class.java)
                    list.add(project)
                }
                intermediateProjectList.value = list
                Log.i("List", intermediateProjectList.value.toString())
            })
    }

    private fun getQuizQuestion(quizName: String) {

        repository.getQuizReference(quizName)
            .addSnapshotListener(EventListener<QuerySnapshot> { value, error ->
                if (error != null) {
                    Log.w(TAG, "Quiz Failed", error)
                    quizList.value = null
                    return@EventListener
                }
                val list: MutableList<Question> = mutableListOf()
                for (question in value!!) {
                    val question = question.toObject(Question::class.java)
                    list.add(question)
                }
                quizList.value = list
            })
    }

    fun addUserToFirebase(user: User) = viewModelScope.launch {
        repository.addUserToFirebase(user)
    }

    private fun getUserDetails(uid: String) {
        val reference = Firebase.firestore.collection("User").document(uid)
        reference.get()
            .addOnSuccessListener {
                if (it != null) {
                    user = it.toObject<User>()!!
                    Log.i(TAG, it.data.toString())

                } else {
                    Log.i(TAG, "error: ")
                }
            }
    }

    fun getUser(uid: String): User? {
        getUserDetails(uid)
        return user
    }

    fun getLearnBlog() : LiveData<List<LearnBlog>> {
        getLearnBlogList()
        Log.i("list", learnBlogList.toString())
        return learnBlogList
    }

    private fun getLearnBlogList() {
        repository.getLearnReference()
            .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.w(TAG, "Failed", e)
                    learnBlogList.value = null
                    return@EventListener
                }
                val list: MutableList<LearnBlog> = mutableListOf()
                for (doc in value!!) {
                    val blog = doc.toObject(LearnBlog::class.java)
                    list.add(blog)
                }
                learnBlogList.value = list
                Log.i("List", learnBlogList.value.toString())
            })

    }


}