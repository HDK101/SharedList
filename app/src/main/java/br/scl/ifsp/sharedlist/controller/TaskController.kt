//package br.scl.ifsp.sharedlist.controller
//
//import android.content.Context
//import br.scl.ifsp.sharedlist.model.Task
//import br.scl.ifsp.sharedlist.model.TaskDao
//import br.scl.ifsp.sharedlist.model.TaskRealtimeDatabase
//
//class TaskController() {
//    private val taskDaoImpl = TaskRealtimeDatabase()
//
//    fun insert(task: Task, callback: (tasks: MutableList<Task>) -> Unit) {
//        Thread {
//            taskDaoImpl.create(task)
//            //taskDaoImpl.all(callback)
//        }.start()
//    }
//
//    fun get(id:Int) = taskDaoImpl.retrieve(id)
//
//    fun all(callback: (tasks: MutableList<Task>) -> Unit) {
//        //taskDaoImpl.all(callback)
//    }
//    fun edit(task: Task) {
//        Thread{
//            taskDaoImpl.update(task)
//        }.start()
//    }
//    fun remove(task: Task) {
//        Thread{
//            taskDaoImpl.delete(task)
//        }.start()
//    }
//}