package br.scl.ifsp.sharedlist.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.scl.ifsp.sharedlist.R
import br.scl.ifsp.sharedlist.adapter.TaskAdapter
//import br.scl.ifsp.sharedlist.controller.TaskController
import br.scl.ifsp.sharedlist.databinding.ActivityTasksBinding
import br.scl.ifsp.sharedlist.model.Task
import br.scl.ifsp.sharedlist.model.TaskRealtimeDatabase
import br.scl.ifsp.sharedlist.view.LoginActivity.Companion.LOGGED_WITH_EMAIL_PREFERENCE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class TasksActivity : AppCompatActivity() {
    private val activityTaskBinding: ActivityTasksBinding by lazy {
        ActivityTasksBinding.inflate(
            layoutInflater
        )
    }
    private lateinit var auth: FirebaseAuth

    private val taskRealtimeDatabase = TaskRealtimeDatabase()

    private val tasks = mutableListOf<Task>()

    private val taskAdapter: TaskAdapter by lazy { TaskAdapter(this, tasks) }

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activityTaskBinding.root)

        registerForContextMenu(activityTaskBinding.listViewTasks)

        auth = Firebase.auth

        taskRealtimeDatabase.addObserver {
            updateTasks(it)
        }

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {

                }
            }

        activityTaskBinding.listViewTasks.adapter = taskAdapter

        activityTaskBinding.listViewTasks.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, p3 ->
                val intentUpdate = Intent(this, TaskUpdateActivity::class.java)
                intentUpdate.putExtra(TaskUpdateActivity.TASK_EXTRA, tasks[position])
                activityResultLauncher.launch(intentUpdate)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tasks, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addTask -> {
                activityResultLauncher.launch(Intent(this, TaskCreateActivity::class.java))
                true
            }
            R.id.exit -> {
                val loginIntent = Intent(this, LoginActivity::class.java)
                loginIntent.putExtra(LoginActivity.EMAIL_EXTRA, auth.currentUser?.email)
                auth.signOut()
                startActivity(loginIntent)
                finish()
                true
            }
            else -> false
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menu?.add(Menu.NONE, 1, Menu.NONE, "Atualizar")
        menu?.add(Menu.NONE, 2, Menu.NONE, "Deletar")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        super.onContextItemSelected(item)
        val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
        if (item.itemId == 1) {
            val intentUpdate = Intent(this, TaskUpdateActivity::class.java)
            intentUpdate.putExtra(TaskUpdateActivity.TASK_EXTRA, tasks[position])
            activityResultLauncher.launch(intentUpdate)
        }
        if (item.itemId == 2) {
            val task = tasks[position]
            if (auth.uid == task.userUid && !task.finished) {
                taskRealtimeDatabase.delete(tasks[position]) {
                    Toast.makeText(this, "Tarefa deletada!", Toast.LENGTH_LONG).show()
                }
            } else if (auth.uid != task.userUid) {
                Toast.makeText(this, "Você não é o criador dessa tarefa", Toast.LENGTH_LONG).show()
            } else if (task.finished) {
                Toast.makeText(this, "Essa tarefa já está finalizada, não pode ser deletada", Toast.LENGTH_LONG).show()
            }
        }
        return true
    }

    fun updateTasks(_tasks: MutableList<Task>) {
        tasks.clear()
        tasks.addAll(_tasks)
        taskAdapter.notifyDataSetChanged()
    }
}