package br.scl.ifsp.sharedlist.model

import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class TaskRealtimeDatabase {
    private val TASK_LIST_ROOT_NODE = "tasks"
    private val taskRtDbFbReference = Firebase.database.getReference(TASK_LIST_ROOT_NODE)

    private val tasks: MutableList<Task> = mutableListOf()

    private val observers: MutableList<(tasks: MutableList<Task>) -> Unit> = mutableListOf()

    private val taskListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val tasksHashMap = snapshot.getValue<HashMap<String, Task>>()
            tasks.clear()

            tasksHashMap?.entries?.forEach {
                it.value.id = it.key
                tasks.add(it.value)
            }

            Log.d("TASK", tasks.toString());

            updateObservers()
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w("TASK", "tag cancelled", error.toException());
        }
    }

    init {
        taskRtDbFbReference.addValueEventListener(taskListener)
    }

    fun create(task: Task, callback: () -> Unit) {
        val foundTask = tasks.find { it.title == task.title }

        if (foundTask == null) {
            taskRtDbFbReference.child(task.title).setValue(task).addOnSuccessListener {
                callback()
            }
        }
    }

    fun update(task: Task, callback: () -> Unit) {
        task.title.let {
            taskRtDbFbReference.child(it).setValue(task).addOnSuccessListener {
                callback()
            }
        }
    }

    fun delete(task: Task, callback: () -> Unit) {
        task.title.let { taskRtDbFbReference.child(it).setValue(null).addOnSuccessListener {
            callback()
        } }
    }

    fun addObserver(callback: (MutableList<Task>) -> Unit) {
        observers.add(callback)
    }

    fun updateObservers() {
        observers.forEach {
            it(tasks)
        }
    }
}