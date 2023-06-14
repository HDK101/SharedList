package br.scl.ifsp.sharedlist.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.scl.ifsp.sharedlist.databinding.ActivityTaskUpdateBinding
import br.scl.ifsp.sharedlist.model.Task
import br.scl.ifsp.sharedlist.model.TaskRealtimeDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*

class TaskUpdateActivity : AppCompatActivity() {
    companion object {
        val TASK_EXTRA = "TASK_EXTRA"
    }

    private val activityTaskUpdateActivity: ActivityTaskUpdateBinding by lazy {
        ActivityTaskUpdateBinding.inflate(
            layoutInflater
        )
    }

    private val taskRealtimeDatabase = TaskRealtimeDatabase()

    private var selectedDate = Date()

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activityTaskUpdateActivity.root)

        auth = Firebase.auth

        val task = wrappedGetParcelable<Task>(TASK_EXTRA)

        task.let {
            activityTaskUpdateActivity.editTextTaskTitle.setText(it?.title)
            activityTaskUpdateActivity.editTextTaskDescription.setText(it?.description)
            activityTaskUpdateActivity.editTextDateCreation.setText(it?.dateCreation.toString())
            activityTaskUpdateActivity.editTextUser.setText(auth.currentUser?.email)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityTaskUpdateActivity.textViewEstimatedDate.text = "Data prevista de conclusão: $selectedDate"

        activityTaskUpdateActivity.buttonDatePickerEstimated.setOnClickListener {
            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { datePicker: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    calendar.set(year, monthOfYear, dayOfMonth)
                    selectedDate = calendar.time
                    activityTaskUpdateActivity.textViewEstimatedDate.text = "Data prevista de conclusão: $selectedDate"
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        }

        activityTaskUpdateActivity.buttonSave.setOnClickListener {
            taskRealtimeDatabase.update(
                Task(
                    id = task?.id,
                    title = activityTaskUpdateActivity.editTextTaskTitle.text.toString(),
                    description = activityTaskUpdateActivity.editTextTaskDescription.text.toString(),
                    dateEstimated = selectedDate,
                    userUid = task?.userUid!!
                )
            ) {
                Toast.makeText(this, "Tarefa atualizada", Toast.LENGTH_LONG).show()
                finish()
            }
        }

        activityTaskUpdateActivity.buttonFinish.setOnClickListener {
            taskRealtimeDatabase.update(
                Task(
                    id = task?.id,
                    title = activityTaskUpdateActivity.editTextTaskTitle.text.toString(),
                    description = activityTaskUpdateActivity.editTextTaskDescription.text.toString(),
                    dateEstimated = selectedDate,
                    dateCreation = task?.dateCreation!!,
                    userUid = task.userUid,
                    finished = true
                )
            ) {
                Toast.makeText(this, "Tarefa finalizada", Toast.LENGTH_LONG).show()
                finish()
            }
        }

        val isEnabled = task?.userUid == auth.uid && !task?.finished!!

        activityTaskUpdateActivity.buttonSave.isEnabled = isEnabled
        activityTaskUpdateActivity.editTextTaskDescription.isEnabled = isEnabled
        activityTaskUpdateActivity.buttonFinish.isEnabled = isEnabled
        activityTaskUpdateActivity.buttonDatePickerEstimated.isEnabled = isEnabled
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}