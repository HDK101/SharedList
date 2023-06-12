package br.scl.ifsp.sharedlist.view

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.scl.ifsp.sharedlist.databinding.ActivityTaskCreateBinding
import br.scl.ifsp.sharedlist.model.Task
import br.scl.ifsp.sharedlist.model.TaskRealtimeDatabase

class TaskCreateActivity: AppCompatActivity() {
    private val activityTaskCreateBinding by lazy { ActivityTaskCreateBinding.inflate(layoutInflater) }

    private val taskRealtimeDatabase = TaskRealtimeDatabase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activityTaskCreateBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        taskRealtimeDatabase.create(Task(
            title = "Title lol"
        )) {
            Toast.makeText(this, "hello", Toast.LENGTH_LONG).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}