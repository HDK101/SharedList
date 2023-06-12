package br.scl.ifsp.sharedlist.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import br.scl.ifsp.sharedlist.R
import br.scl.ifsp.sharedlist.adapter.TaskAdapter
//import br.scl.ifsp.sharedlist.controller.TaskController
import br.scl.ifsp.sharedlist.databinding.ActivityTasksBinding
import br.scl.ifsp.sharedlist.model.Task
import br.scl.ifsp.sharedlist.model.TaskRealtimeDatabase
import com.google.firebase.auth.FirebaseAuth
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

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activityTaskBinding.root)

        taskRealtimeDatabase.addObserver {
            Log.d("TASK", it.toString())
            updateTasks(it)
        }

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {

            }
        }

        activityTaskBinding.listViewTasks.adapter = taskAdapter

        activityTaskBinding.listViewTasks.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, p3 ->

            }

        //updateTasks(tasks)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tasks, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        return when(item.itemId){
            R.id.addTask -> {
                activityResultLauncher.launch(Intent(this, TaskCreateActivity::class.java))
                true
            }
            R.id.exit -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> false
        }
    }

    fun updateTasks(_tasks: MutableList<Task>) {
        tasks.clear()
        tasks.addAll(_tasks)
        taskAdapter.notifyDataSetChanged()
    }
}