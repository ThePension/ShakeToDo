package ch.hearc.shaketodo.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todos")
    fun getAll(): LiveData<List<ToDo>>

    @Query("SELECT * FROM todos WHERE id IN (:todoids)")
    fun loadAllByIds(todoids: IntArray): List<ToDo>

    @Query("SELECT * FROM todos WHERE id=:todoid")
    fun findById(todoid: Long): ToDo

    @Insert
    fun insertAll(vararg todos: ToDo)

    @Delete
    fun delete(todo: ToDo)
}