package br.scl.ifsp.sharedlist.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import br.scl.ifsp.sharedlist.adapter.TaskAdapter
//import br.scl.ifsp.sharedlist.controller.TaskController
import br.scl.ifsp.sharedlist.databinding.ActivityTasksBinding
import br.scl.ifsp.sharedlist.model.Task
import br.scl.ifsp.sharedlist.model.TaskRealtimeDatabase
import java.time.LocalDate
import java.util.Date

class TasksActivity : AppCompatActivity() {
    private val activityTaskBinding: ActivityTasksBinding by lazy {
        ActivityTasksBinding.inflate(
            layoutInflater
        )
    }

    private val taskRealtimeDatabase = TaskRealtimeDatabase()

    private val tasks = mutableListOf<Task>()

    private val taskAdapter: TaskAdapter by lazy { TaskAdapter(this, tasks) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activityTaskBinding.root)

        taskRealtimeDatabase.addObserver {
            Log.d("TASK", it.toString())
            updateTasks(it)
        }

        activityTaskBinding.listViewTasks.adapter = taskAdapter

        taskRealtimeDatabase.create(Task()) {
            Toast.makeText(this, "hello", Toast.LENGTH_LONG).show()
        }

        //updateTasks(tasks)
    }

    fun updateTasks(_tasks: MutableList<Task>) {
        tasks.clear()
        tasks.addAll(_tasks)
        taskAdapter.notifyDataSetChanged()
    }
}