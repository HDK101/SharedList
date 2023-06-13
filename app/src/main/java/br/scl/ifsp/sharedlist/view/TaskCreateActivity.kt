package br.scl.ifsp.sharedlist.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.scl.ifsp.sharedlist.databinding.ActivityTaskCreateBinding
import br.scl.ifsp.sharedlist.model.Task
import br.scl.ifsp.sharedlist.model.TaskRealtimeDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import java.util.Date

class TaskCreateActivity: AppCompatActivity() {
    private val activityTaskCreateBinding by lazy { ActivityTaskCreateBinding.inflate(layoutInflater) }

    private val taskRealtimeDatabase = TaskRealtimeDatabase()

    private var selectedDate: Date = Date()

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activityTaskCreateBinding.root)

        auth = Firebase.auth

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityTaskCreateBinding.textViewEstimatedDate.text = "Data prevista de conclusão: $selectedDate"

        activityTaskCreateBinding.buttonDatePickerEstimated.setOnClickListener {
            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { datePicker: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    calendar.set(year, monthOfYear, dayOfMonth)
                    selectedDate = calendar.time
                    activityTaskCreateBinding.textViewEstimatedDate.text = "Data prevista de conclusão: $selectedDate"
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        }

        activityTaskCreateBinding.buttonSave.setOnClickListener {
            taskRealtimeDatabase.create(Task(
                title = activityTaskCreateBinding.editTextTaskTitle.text.toString(),
                description = activityTaskCreateBinding.editTextTaskDescription.text.toString(),
                dateEstimated = selectedDate,
                userUid = auth.uid!!
            )) {
                Toast.makeText(this, "Tarefa criada", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}