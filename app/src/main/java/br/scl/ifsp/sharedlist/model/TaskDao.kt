package br.scl.ifsp.sharedlist.model

import androidx.room.*

interface TaskDao {
    @Insert
    fun create(contact: Task)

    @Query("SELECT * FROM Task WHERE id = :id")
    fun retrieve(id: Int): Task?

    @Query("SELECT * FROM Task")
    fun all(): MutableList<Task>

    @Update
    fun update(contact: Task): Int

    @Delete
    fun delete(contact: Task): Int
}