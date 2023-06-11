package br.scl.ifsp.sharedlist.model

import androidx.room.*

interface TaskDao {
    fun create(task: Task)

    fun retrieve(id: Int): Task?

    fun all()
    fun update(task: Task): Int

    fun delete(task: Task): Int
}