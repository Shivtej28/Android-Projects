package com.shivtej.androidprojects.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.shivtej.androidprojects.repo.ProjectRepository
import com.shivtej.androidprojects.models.Project

class ProjectViewModel : ViewModel() {

    val TAG = "ViewModel"

    var basicProjectList: MutableLiveData<List<Project>> = MutableLiveData()
    var intermediateProjectList: MutableLiveData<List<Project>> = MutableLiveData()
    var advanceProjectList: MutableLiveData<List<Project>> = MutableLiveData()

    val repository = ProjectRepository()

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

    fun getIProjects(){
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


}