package com.example.tareasapp.addTareas.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {

    @Query("SELECT * from TareaEntity")
    fun getTareas(): Flow<List<TareaEntity>>

    @Insert
    suspend fun addTarea(item:TareaEntity)

    @Update
    suspend fun updateTarea(item: TareaEntity)

    @Delete
    suspend fun deleteTarea(item: TareaEntity)

    @Query("DELETE FROM TareaEntity")
    suspend fun deleteAll()
}