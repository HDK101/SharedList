package br.scl.ifsp.sharedlist.controller

import android.content.Context
import br.scl.ifsp.sharedlist.model.Task
import br.scl.ifsp.sharedlist.model.TaskDao
import br.scl.ifsp.sharedlist.model.TaskDaoRtDbFb

class TaskController() {
    private val taskDaoImpl: TaskDao = TaskDaoRtDbFb()

    fun insert(task: Task, callback: (tasks: MutableList<Task>) -> Unit) {

        Thread {
            taskDaoImpl.create(task)
            callback(taskDaoImpl.all())
        }.start()
    }

    fun get(id:Int) = taskDaoImpl.retrieve(id)

    fun all(callback: (tasks: MutableList<Task>) -> Unit) {
        Thread {
            callback(taskDaoImpl.all())
        }.start()
    }
    fun edit(task: Task) {
        Thread{
            taskDaoImpl.update(task)
        }.start()
    }
    fun remove(task: Task) {
        Thread{
            taskDaoImpl.delete(task)
        }.start()
    }
}