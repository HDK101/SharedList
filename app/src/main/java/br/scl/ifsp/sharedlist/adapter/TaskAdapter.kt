package br.scl.ifsp.sharedlist.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.scl.ifsp.sharedlist.R
import br.scl.ifsp.sharedlist.databinding.TileTaskBinding
import br.scl.ifsp.sharedlist.model.Task

class TaskAdapter(
    context: Context,
    private val taskList: MutableList<Task>
): ArrayAdapter<Task> (context, R.layout.tile_task, taskList){
    private lateinit var tileTaskBinding: TileTaskBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val task: Task = taskList[position]
        var tileTaskView = convertView
        if(tileTaskView == null){
            //infla uma nova célula
            tileTaskBinding = TileTaskBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false

            )
            tileTaskView = tileTaskBinding.root

            val tileTaskViewHolder = TileTaskViewHolder(
                tileTaskBinding.textViewName,
            )

            tileTaskView.tag = tileTaskViewHolder
        }

        with(tileTaskView.tag as TileTaskViewHolder){
            textViewName.text = task.title
        }

        return tileTaskView
    }

    private data class TileTaskViewHolder(
        val textViewName: TextView,
    )
}