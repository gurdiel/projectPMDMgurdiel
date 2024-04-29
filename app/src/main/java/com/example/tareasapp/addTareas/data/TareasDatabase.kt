package com.example.tareasapp.addTareas.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TareaEntity::class], version = 1)
abstract class TareasDatabase: RoomDatabase() {

    abstract fun tareaDao():TareaDao

}