package com.example.tareasapp.addTareas.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TareaEntity(
    @PrimaryKey
    val id: Int,
    val tarea: String,
    var selected: Boolean
)
