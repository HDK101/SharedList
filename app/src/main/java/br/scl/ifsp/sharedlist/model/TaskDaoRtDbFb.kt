package br.scl.ifsp.sharedlist.model

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TaskDaoRtDbFb: TaskDao {
    private val TASK_LIST_ROOT_NODE = "contactList"
    private val taskRtDbFbReference = Firebase.database.getReference(TASK_LIST_ROOT_NODE)

    init {
        taskRtDbFbReference.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        taskRtDbFbReference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun create(contact: Task) {
        TODO("Not yet implemented")
    }

    override fun retrieve(id: Int): Task? {
        TODO("Not yet implemented")
    }

    override fun all(): MutableList<Task> {
        TODO("Not yet implemented")
    }

    override fun update(contact: Task): Int {
        TODO("Not yet implemented")
    }

    override fun delete(contact: Task): Int {
        TODO("Not yet implemented")
    }
}